package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.SecurityConfig;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;
// Pamiętaj o mockowaniu beanów security, jeśli są wymagane przez SecurityConfig
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomLogoutHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomAuthenticationSuccessHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.CustomUserDetailsService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(REST_CartController.class)
@Import(SecurityConfig.class)
class REST_CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private SessionCart sessionCart;
    @MockitoBean private ProductRepository productRepository; // Używamy repozytorium bezpośrednio w kontrolerze

    // Mocki dla Security
    @MockitoBean private CustomUserDetailsService userDetailsService;
    @MockitoBean private CustomLogoutHandler logoutHandler;
    @MockitoBean private CustomAuthenticationSuccessHandler successHandler;

    @Test
    void addProductToCart_ShouldAddToSessionAndRedirect() throws Exception {
        // GIVEN
        Long prodId = 1L;
        Product product = new Product();
        product.setId(prodId);
        product.setName("Test");
        product.setPrice(BigDecimal.ONE);

        when(productRepository.findById(prodId)).thenReturn(Optional.of(product));

        // WHEN
        mockMvc.perform(post("/cart/add/{id}", prodId))
                .andExpect(status().is3xxRedirection()) // Oczekujemy przekierowania
                .andExpect(redirectedUrl("/kupno-sprzedaz"));

        // THEN
        verify(sessionCart).addProduct(any(DTO_Product.class));
    }
}