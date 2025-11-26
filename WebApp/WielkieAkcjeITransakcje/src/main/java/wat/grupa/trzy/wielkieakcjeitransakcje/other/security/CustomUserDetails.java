package wat.grupa.trzy.wielkieakcjeitransakcje.other.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UserData user;

    public CustomUserDetails(UserData user) {
        this.user = user;
    }

    // Metoda, aby łatwo dostać się do naszego oryginalnego obiektu UserData
    public UserData getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.emptyList(); }
    @Override
    public String getPassword() { return user.getPasswordHash(); }
    @Override
    public String getUsername() { return user.getEmail(); }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}