package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomAuthenticationSuccessHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomLogoutHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.SecurityConfig;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.AuctionService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.CustomUserDetailsService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(REST_AuctionController.class)
@Import(SecurityConfig.class)
class REST_AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private AuctionService auctionService;
    @MockitoBean private UserDataRepository userDataRepository;
    @MockitoBean private SessionCart sessionCart;

    @MockitoBean private CustomUserDetailsService userDetailsService;
    @MockitoBean private CustomLogoutHandler logoutHandler;
    @MockitoBean private CustomAuthenticationSuccessHandler successHandler;

    @Test
    @DisplayName("GET /auction/szczegoly: Powinien zwrócić widok szczegółów z atrybutami")
    @WithMockUser
    void getAuctionDetails_ShouldReturnView() throws Exception {
        // GIVEN
        Long id = 1L;

        // --- POPRAWKA: Tworzymy sprzedawcę i produkt ---
        UserData seller = new UserData();
        seller.setUsername("testSeller");

        Product product = new Product();
        product.setSeller(seller); // Ustawiamy sprzedawcę, żeby Thymeleaf nie rzucał NPE
        product.setName("Test Product");
        product.setDescription("Opis");

        Auction auction = new Auction();
        auction.setId(id);
        auction.setAktualnaCena(BigDecimal.TEN);
        auction.setProduct(product);
        auction.setOferty(Collections.emptyList());
        // ---------------------------------------------

        when(auctionService.getAuctionById(id)).thenReturn(Optional.of(auction));

        // WHEN & THEN
        mockMvc.perform(get("/auction/szczegoly/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("auctionSzczegoly"))
                .andExpect(model().attributeExists("auction"))
                .andExpect(model().attributeExists("offers"));
    }

    @Test
    @DisplayName("POST /licytuj: Powinien przekierować z sukcesem, gdy oferta przyjęta")
    @WithMockUser(username = "test@user.pl")
    void postBid_ShouldRedirectSuccess_WhenBidAccepted() throws Exception {
        Long auctionId = 1L;
        BigDecimal kwota = BigDecimal.valueOf(100);
        UserData user = new UserData();
        user.setId(10L);
        user.setEmail("test@user.pl");

        when(userDataRepository.findByEmail("test@user.pl")).thenReturn(Optional.of(user));
        when(auctionService.zlozOferte(eq(auctionId), eq(10L), eq(kwota))).thenReturn(true);

        mockMvc.perform(post("/auction/licytuj/{id}", auctionId)
                        .param("kwota", "100")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auction/szczegoly/1?success=OfertaPrzyjeta"));
    }

    @Test
    @DisplayName("POST /licytuj: Powinien przekierować z błędem, gdy oferta odrzucona")
    @WithMockUser(username = "test@user.pl")
    void postBid_ShouldRedirectError_WhenBidRejected() throws Exception {
        Long auctionId = 1L;
        UserData user = new UserData();
        user.setId(10L);

        when(userDataRepository.findByEmail("test@user.pl")).thenReturn(Optional.of(user));
        when(auctionService.zlozOferte(any(), any(), any())).thenReturn(false);

        mockMvc.perform(post("/auction/licytuj/{id}", auctionId)
                        .param("kwota", "50")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auction/szczegoly/1?error=OfertaOdrzucona"));
    }

    @Test
    @DisplayName("POST /licytuj: Powinien przekierować do logowania, gdy brak usera")
    void postBid_ShouldRedirectToLogin_WhenUnauthenticated() throws Exception {
        mockMvc.perform(post("/auction/licytuj/1")
                        .param("kwota", "100")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}