package wat.grupa.trzy.wielkieakcjeitransakcje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.UserDataRepository;

@Service
public class UserService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }
}
