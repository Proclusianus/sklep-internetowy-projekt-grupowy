package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Oferta;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.AuctionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.OfertaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.AuctionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock private AuctionRepository auctionRepository;
    @Mock private OfertaRepository ofertaRepository;
    @Mock private UserDataRepository userDataRepository;
    @Mock private ProductRepository productRepository; // Potrzebne do zapisu zmiany właściciela produktu

    @InjectMocks
    private AuctionService auctionService;

    // --- TESTY SKŁADANIA OFERT ---

    @Test
    @DisplayName("zlozOferte: Powinien przyjąć ofertę, gdy jest wyższa od aktualnej i aukcja trwa")
    void zlozOferte_ShouldSuccess_WhenBidIsHighEnough() {
        // GIVEN
        Long auctionId = 1L;
        Long userId = 10L;
        BigDecimal currentPrice = BigDecimal.valueOf(100);
        BigDecimal newBid = BigDecimal.valueOf(150);

        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setAktualnaCena(currentPrice);
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().plusHours(2)); // Aukcja trwa

        UserData user = new UserData();
        user.setId(userId);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userDataRepository.findById(userId)).thenReturn(Optional.of(user));

        // WHEN
        boolean result = auctionService.zlozOferte(auctionId, userId, newBid);

        // THEN
        assertTrue(result);
        assertEquals(newBid, auction.getAktualnaCena()); // Cena zaktualizowana w obiekcie
        verify(ofertaRepository).save(any(Oferta.class)); // Oferta zapisana
        verify(auctionRepository).save(auction); // Aukcja zaktualizowana
    }

    @Test
    @DisplayName("zlozOferte: Powinien odrzucić ofertę, gdy jest za niska")
    void zlozOferte_ShouldFail_WhenBidIsLow() {
        // GIVEN
        Auction auction = new Auction();
        auction.setAktualnaCena(BigDecimal.valueOf(200));
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().plusHours(1));

        when(auctionRepository.findById(any())).thenReturn(Optional.of(auction));
        when(userDataRepository.findById(any())).thenReturn(Optional.of(new UserData()));

        // WHEN
        boolean result = auctionService.zlozOferte(1L, 1L, BigDecimal.valueOf(150));

        // THEN
        assertFalse(result);
        verify(ofertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("zlozOferte: Powinien zamknąć aukcję i odrzucić ofertę, jeśli czas minął")
    void zlozOferte_ShouldCloseAuction_WhenTimeExpired() {
        // GIVEN
        Auction auction = new Auction();
        auction.setId(1L);
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().minusMinutes(1)); // Czas minął
        // Aukcja musi mieć produkt, żeby logika zamknięcia zadziałała (przeniesienie własności)
        Product product = new Product();
        auction.setProduct(product);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userDataRepository.findById(any())).thenReturn(Optional.of(new UserData()));

        // WHEN
        boolean result = auctionService.zlozOferte(1L, 1L, BigDecimal.valueOf(1000));

        // THEN
        assertFalse(result, "Oferta powinna zostać odrzucona bo aukcja wygasła");
        assertFalse(auction.isAktywna(), "Aukcja powinna zostać dezaktywowana");
        verify(auctionRepository).save(auction); // Zapisanie zamknięcia
    }

    // --- TESTY ZAMYKANIA AUKCJI ---

    @Test
    @DisplayName("sprawdzCzyZakonczona: Powinien przenieść własność produktu na zwycięzcę")
    void sprawdzCzyZakonczona_ShouldTransferOwnership() {
        // GIVEN
        UserData seller = new UserData();
        seller.setUsername("Sprzedawca");

        UserData winner = new UserData();
        winner.setUsername("Zwyciezca");

        Product product = new Product();
        product.setSeller(seller); // Początkowy właściciel

        Auction auction = new Auction();
        auction.setId(100L);
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().minusHours(1)); // Wygasła
        auction.setProduct(product);

        // Symulacja wygrywającej oferty
        Oferta winningBid = new Oferta();
        winningBid.setLicytujacy(winner);
        winningBid.setKwota(BigDecimal.valueOf(500));
        // Lista ofert (Service bierze pierwszą z listy, która powinna być posortowana w encji, tu symulujemy listę)
        List<Oferta> oferty = new ArrayList<>();
        oferty.add(winningBid);
        auction.setOferty(oferty);

        // WHEN
        auctionService.sprawdzCzyZakonczona(auction);

        // THEN
        assertFalse(auction.isAktywna());
        verify(productRepository).save(product); // Produkt musi zostać zapisany z nowym właścicielem

        // Sprawdzamy czy właściciel się zmienił
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        assertEquals(winner, productCaptor.getValue().getSeller());
    }

    @Test
    @DisplayName("sprawdzCzyZakonczona: Nie powinien zmieniać właściciela, gdy brak ofert")
    void sprawdzCzyZakonczona_ShouldNotTransfer_WhenNoBids() {
        // GIVEN
        UserData seller = new UserData();
        Product product = new Product();
        product.setSeller(seller);

        Auction auction = new Auction();
        auction.setAktywna(true);
        auction.setDataZakonczenia(LocalDateTime.now().minusHours(1));
        auction.setProduct(product);
        auction.setOferty(new ArrayList<>()); // Pusta lista ofert

        // WHEN
        auctionService.sprawdzCzyZakonczona(auction);

        // THEN
        assertFalse(auction.isAktywna()); // Aukcja się zamyka
        verify(productRepository, never()).save(any()); // Ale produkt nie jest aktualizowany (nie zmienia właściciela)
        assertEquals(seller, product.getSeller());
    }
}