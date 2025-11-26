package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// NOWY IMPORT DLA SPRING BOOT 3.4+
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginRequest;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginResponse;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomAuthenticationSuccessHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomLogoutHandler;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.SecurityConfig;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.SessionCart;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.ApiLogoutService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.LoginService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.RegistrationService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.UserProfileService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(REST_UserController.class)
@Import(SecurityConfig.class)
class REST_UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ZMIANA: @MockitoBean zamiast @MockBean
    @MockitoBean private RegistrationService registrationService;
    @MockitoBean private SessionCart sessionCart;
    @MockitoBean private LoginService loginService;
    @MockitoBean private UserProfileService userProfileService;
    @MockitoBean private ApiLogoutService apiLogoutService;
    @MockitoBean private CustomLogoutHandler clh;
    @MockitoBean private CustomAuthenticationSuccessHandler cash;

    @Test
    void login_ShouldReturn200_WhenCredentialsAreCorrect() throws Exception {
        // GIVEN
        DTO_LoginRequest loginRequest = new DTO_LoginRequest("test@test.com", "pass");

        // POPRAWKA: Mockujemy zwracanie DTO, a nie Sessions
        DTO_LoginResponse mockResponse = new DTO_LoginResponse("uuid-123", 1L, "test", "OSOBA_FIZYCZNA");

        when(loginService.loginUser(any())).thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
}