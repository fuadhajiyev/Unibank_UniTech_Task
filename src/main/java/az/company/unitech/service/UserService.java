package az.company.unitech.service;

import az.company.unitech.entity.User;
import az.company.unitech.exception.AccountNotFoundException;
import az.company.unitech.exception.UserAlreadyExistException;
import az.company.unitech.model.CreateUserRequestModel;
import az.company.unitech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(CreateUserRequestModel model) {
        // check if pin already exists in database
        // then throw an exception
        Optional<User> userOptional = userRepository.findByPin(model.getPin());
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistException("There's a user with the given pin");
        }

        User user = new User(model.getFullName(), passwordEncoder.encode(model.getPassword()), model.getPin());
        return userRepository.save(user);
    }

    /**
     * Returns authenticated User Object
     */
    public User getAuthUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        return userRepository.findByPin(username).get();
    }


}
