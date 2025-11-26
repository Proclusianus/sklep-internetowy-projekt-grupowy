package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.WebsiteMapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomAuthenticationSuccessHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomLogoutHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.SecurityConfig;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.OrderService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.payment.PaymentService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.CustomUserDetailsService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MAP_UserController.class)
@Import(SecurityConfig.class)
class MAP_UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private PaymentService paymentService;
    @MockitoBean private OrderService orderService;
    @MockitoBean private SessionCart sessionCart;

    @MockitoBean private CustomUserDetailsService userDetailsService;
    @MockitoBean private CustomLogoutHandler logoutHandler;
    @MockitoBean private CustomAuthenticationSuccessHandler successHandler;

    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        // --- POPRAWKA: Przygotowanie poprawnego CustomUserDetails ---
        UserData user = new UserData();
        user.setId(1L);
        user.setEmail("test@test.pl");
        user.setUsername("test@test.pl");
        user.setPasswordHash("pass");
        user.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA); // Ważne dla header.html

        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    @DisplayName("GET /wallet: Powinien wyświetlić portfel dla zalogowanego użytkownika")
    void getWalletPage_ShouldShowWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(1L);

        when(paymentService.getUserWallet("test@test.pl")).thenReturn(wallet);

        mockMvc.perform(get("/wallet")
                        .with(user(customUserDetails))) // Używamy CustomUserDetails
                .andExpect(status().isOk())
                .andExpect(view().name("userpanel/wallet"))
                .andExpect(model().attributeExists("wallet"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("POST /wallet/topup: Powinien przekierować z sukcesem po doładowaniu")
    void topUpWallet_ShouldRedirectWithSuccess() throws Exception {
        mockMvc.perform(post("/wallet/topup")
                        .with(user(customUserDetails))
                        .with(csrf())
                        .param("amount", "1000")
                        .param("methodId", "card_1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wallet"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("POST /wallet/add-card: Powinien zwrócić błąd, gdy numer karty jest zły")
    void addCard_ShouldRedirectWithError_WhenValidationFails() throws Exception {
        doThrow(new IllegalArgumentException("Zły numer karty"))
                .when(paymentService).addCreditCard(anyString(), anyString());

        mockMvc.perform(post("/wallet/add-card")
                        .with(user(customUserDetails))
                        .with(csrf())
                        .param("cardNumber", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wallet/add-card"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}