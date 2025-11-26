package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public CustomUserDetailsService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserData user = userDataRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o emailu: " + email));

        return new CustomUserDetails(user);
    }
}