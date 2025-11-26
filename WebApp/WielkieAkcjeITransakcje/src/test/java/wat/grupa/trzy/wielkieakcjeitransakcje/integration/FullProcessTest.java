package wat.grupa.trzy.wielkieakcjeitransakcje.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Address;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Oferta;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.AuctionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.OfertaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment.CardRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment.WalletRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.EmailService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user; // Ważne!
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FullProcessTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager entityManager;

    @Autowired private UserDataRepository userDataRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OfertaRepository ofertaRepository;
    @Autowired private CardRepository cardRepository;

    // --- ZMIANA 1: Mockujemy cały serwis mailowy ---
    // To zapobiega próbom łączenia się z serwerem SMTP i błędom NullPointer przy tworzeniu MimeMessage
    @MockitoBean private EmailService emailService;

    @Test
    @DisplayName("SCENARIUSZ: Rejestracja -> Dodanie Karty -> Doładowanie Portfela")
    void fullWalletScenario() throws Exception {
        // 1. Przygotowanie użytkownika
        UserData user = new UserData();
        user.setEmail("nowy@test.pl");
        user.setUsername("nowy@test.pl");
        user.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
        user.setPasswordHash("hash");
        user.setEnabled(true);

        Wallet wallet = new Wallet();
        wallet.setUserData(user);
        wallet.setTransactionDataList(new ArrayList<>());
        wallet.setOwnedCards(new ArrayList<>());
        user.setWallet(wallet);

        userDataRepository.save(user);
        walletRepository.save(wallet);

        // --- ZMIANA 2: Używamy CustomUserDetails zamiast @WithMockUser ---
        // Dzięki temu kontroler otrzyma obiekt właściwego typu i nie rzuci NPE
        CustomUserDetails authUser = new CustomUserDetails(user);

        // 2. Dodanie Karty
        mockMvc.perform(post("/wallet/add-card")
                        .with(user(authUser)) // Symulacja zalogowania konkretnym obiektem
                        .with(csrf())
                        .param("cardNumber", "1234-5678-1234-5678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));

        // Weryfikacja w bazie
        Wallet dbWallet = walletRepository.findByUserData_Email("nowy@test.pl").orElseThrow();
        assertFalse(dbWallet.getOwnedCards().isEmpty());
        CardData savedCard = dbWallet.getOwnedCards().get(0);
        assertTrue(savedCard.getMaskedNumber().endsWith("5678"));

        // 3. Doładowanie Portfela
        mockMvc.perform(post("/wallet/topup")
                        .with(user(authUser))
                        .with(csrf())
                        .param("amount", "500")
                        .param("methodId", "card_" + savedCard.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("successMessage"));

        entityManager.flush();
        entityManager.clear();

        Wallet dbWalletAfterTopUp = walletRepository.findById(dbWallet.getId()).orElseThrow();
        assertEquals(500L, dbWalletAfterTopUp.getBalance());
        assertEquals(1, dbWalletAfterTopUp.getTransactionDataList().size());
    }

    @Test
    @DisplayName("SCENARIUSZ: Aukcja -> Złożenie Oferty -> Zmiana Ceny")
    @WithMockUser(username = "licytujacy@test.pl") // Tutaj @WithMockUser jest OK, bo REST Controller używa Principal
    void auctionFlowScenario() throws Exception {
        // GIVEN
        UserData seller = new UserData();
        seller.setEmail("sprzedawca@test.pl");
        seller.setUsername("sprzedawca");
        seller.setPasswordHash("pass");
        userDataRepository.save(seller);

        UserData bidder = new UserData();
        bidder.setEmail("licytujacy@test.pl");
        bidder.setUsername("licytujacy@test.pl");
        bidder.setPasswordHash("pass");
        userDataRepository.save(bidder);

        Product product = new Product();
        product.setName("Laptop Gamingowy");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setSeller(seller);
        productRepository.save(product);

        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setAktualnaCena(BigDecimal.valueOf(1000));
        auction.setCenaWywolawcza(BigDecimal.valueOf(1000));
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().plusDays(7));
        auctionRepository.save(auction);

        Long auctionId = auction.getId();

        // WHEN
        mockMvc.perform(post("/auction/licytuj/{id}", auctionId)
                        .with(csrf())
                        .param("kwota", "1200"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auction/szczegoly/" + auctionId + "?success=OfertaPrzyjeta"));

        entityManager.flush();
        entityManager.clear();

        // THEN
        Auction dbAuction = auctionRepository.findById(auctionId).orElseThrow();
        assertEquals(0, dbAuction.getAktualnaCena().compareTo(BigDecimal.valueOf(1200)));

        List<Oferta> ofertyWBazie = ofertaRepository.findAll();
        boolean ofertaIstnieje = ofertyWBazie.stream()
                .anyMatch(o -> o.getAuction().getId().equals(auctionId)
                        && o.getLicytujacy().getEmail().equals("licytujacy@test.pl")
                        && o.getKwota().compareTo(BigDecimal.valueOf(1200)) == 0);

        assertTrue(ofertaIstnieje, "Oferta powinna zostać zapisana w bazie danych");
    }

    @Test
    @DisplayName("API: Rejestracja użytkownika (walidacja JSON)")
    void registerApiScenario() throws Exception {
        // @MockitoBean private EmailService emailService; - załatwia sprawę maila tutaj

        DTO_Registration registration = new DTO_Registration();
        registration.setEmail("api_user@test.com");
        registration.setUsername("api_user");
        registration.setPassword("StrongPass1!");
        registration.setConfirmPassword("StrongPass1!");
        registration.setAccountType("osoba");
        registration.setFirstName("Jan");
        registration.setLastName("Nowak");
        registration.setPhoneNumber("+48111222333");

        DTO_Address address = new DTO_Address();
        address.setCountry("POLSKA");
        address.setCity("Warszawa");
        address.setStreet("Marszałkowska");
        address.setHouseNumber("1");
        address.setZipCode("00-001");
        registration.setAddress(address);

        mockMvc.perform(post("/api/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk());

        assertTrue(userDataRepository.existsByEmail("api_user@test.com"));

        entityManager.flush();
        entityManager.clear();

        UserData savedUser = userDataRepository.findByEmail("api_user@test.com").orElseThrow();
        assertFalse(savedUser.getConfirmationTokens().isEmpty());
    }
}