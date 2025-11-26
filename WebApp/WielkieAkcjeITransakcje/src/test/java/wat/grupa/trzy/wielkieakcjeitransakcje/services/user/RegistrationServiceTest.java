package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Address;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.EmailService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;


    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registerNewUser_ShouldSaveUser_WhenDataIsValid() {
        // GIVEN
        DTO_Registration request = new DTO_Registration();
        request.setEmail("test@test.com");
        request.setUsername("testuser");
        request.setPassword("StrongPass1!");
        request.setConfirmPassword("StrongPass1!");
        request.setAccountType("osoba");
        request.setFirstName("Jan");
        request.setLastName("Kowalski");
        request.setPhoneNumber("+48123456789");

        DTO_Address address = new DTO_Address();
        address.setStreet("Ulica");
        address.setHouseNumber("1");
        address.setZipCode("00-001");
        address.setCity("Miasto");
        address.setCountry("Polska");
        request.setAddress(address);
        address.setAdminLevel1("Mazowieckie");
        address.setAdminLevel2("Warszawa");
        address.setAdminLevel3("Centrum");
        address.setAdminLevel4("Miasto");

        when(userDataRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userDataRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");


        registrationService.registerNewUser(request);

        verify(userDataRepository, times(1)).save(any(UserData.class));

        verify(passwordEncoder, times(1)).encode("StrongPass1!");
    }

    @Test
    void registerNewUser_ShouldThrowException_WhenEmailExists() {

        DTO_Registration request = new DTO_Registration();
        request.setEmail("zajety@test.com");


        when(userDataRepository.existsByEmail(request.getEmail())).thenReturn(true);


        assertThrows(IllegalArgumentException.class, () -> {
            registrationService.registerNewUser(request);
        });


        verify(userDataRepository, never()).save(any());
    }

    @Test
    void registerNewUser_ShouldThrowException_WhenPasswordsDoNotMatch() {

        DTO_Registration request = new DTO_Registration();
        request.setEmail("nowy@test.com");
        request.setUsername("nowy");
        request.setPassword("Haslo1!");
        request.setConfirmPassword("InneHaslo2@");

        when(userDataRepository.existsByEmail(any())).thenReturn(false);
        when(userDataRepository.existsByUsername(any())).thenReturn(false);


        assertThrows(IllegalArgumentException.class, () -> {
            registrationService.registerNewUser(request);
        });
    }
}