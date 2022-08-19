package az.company.unitech.service;

import az.company.unitech.entity.User;
import az.company.unitech.exception.AccountNotFoundException;
import az.company.unitech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByPin(username);
        if (userOptional.isEmpty()) {
            throw new AccountNotFoundException("The given pin is not valid");
        }

        return new org.springframework.security.core.userdetails.User(userOptional.get().getPin(), userOptional.get().getPassword(), Collections.emptyList());
    }
}
