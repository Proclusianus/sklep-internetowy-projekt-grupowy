package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.user.RegistrationService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class REST_UserController {

    private final RegistrationService registrationService;

    @Autowired
    public REST_UserController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody DTO_Registration request) {
        registrationService.registerNewUser(request);
        return ResponseEntity.ok("Użytkownik zarejestrowany pomyślnie!");
    }
}
