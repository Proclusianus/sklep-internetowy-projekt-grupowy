package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginRequest;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_LoginResponse;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Sessions;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.ApiLogoutService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.LoginService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.RegistrationService;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_UserProfile;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.UserProfileService;

import java.nio.file.AccessDeniedException;
import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/api")
public class REST_UserController {

    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final UserProfileService userProfileService;
    private final ApiLogoutService apiLogoutService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    public REST_UserController(RegistrationService r, LoginService l, UserProfileService u, ApiLogoutService al) { // ZAKTUALIZOWANY KONSTRUKTOR
        this.registrationService = r;
        this.loginService = l;
        this.userProfileService = u;
        this.apiLogoutService = al;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody DTO_Registration request) {
        registrationService.registerNewUser(request);
        return ResponseEntity.ok("Użytkownik zarejestrowany pomyślnie!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody DTO_LoginRequest request) {
        try {
            DTO_LoginResponse response = loginService.loginUser(request);

            return ResponseEntity.ok(response);
        } catch (LoginException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUserApi(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String sessionId = authorizationHeader.substring(7);
        apiLogoutService.logout(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Brak lub niepoprawny nagłówek autoryzacji.");
        }

        String sessionId = authorizationHeader.substring(7);

        try {
            DTO_UserProfile userProfile = userProfileService.getUserProfileBySessionId(sessionId);
            return ResponseEntity.ok(userProfile);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String token) {
        try {
            registrationService.confirmToken(token);
            return "<h1>Sukces!</h1>" +
                    "<p>Twoje konto zostało aktywowane.</p>" +
                    "<p><a href=\"" + baseUrl + "/login\">Kliknij tutaj, aby się zalogować</a></p>";
        } catch (IllegalStateException e) {
            return "<h1>Błąd</h1><p>" + e.getMessage() + "</p>";
        }
    }
}
