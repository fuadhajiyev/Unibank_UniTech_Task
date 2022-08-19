package az.company.unitech.service;

import az.company.unitech.entity.User;
import az.company.unitech.exception.UserAlreadyExistException;
import az.company.unitech.model.CreateUserRequestModel;
import az.company.unitech.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void saveUserSuccessfully() {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel("Fuad", "password123", "12345");

        Mockito.when(passwordEncoder.encode(createUserRequestModel.getPassword()))
                .thenReturn(new BCryptPasswordEncoder().encode(createUserRequestModel.getPassword()));

        Mockito.when(userRepository.findByPin(createUserRequestModel.getPin()))
                .thenReturn(Optional.empty());

        User model = new User(1L, createUserRequestModel.getFullName(), createUserRequestModel.getPassword(), createUserRequestModel.getPin());
        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(model);

        User user = userService.saveUser(createUserRequestModel);
        assertThat(user.getFullName()).isEqualTo(createUserRequestModel.getFullName());
        assertThat(user.getPin()).isEqualTo(createUserRequestModel.getPin());
    }


    @Test
    public void saveUser_WhenGivenAlreadyExistPin_ShouldThrowUserAlreadyExistException() {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel("Fuad", "password123", "12345");

        User model = new User(1L, createUserRequestModel.getFullName(), createUserRequestModel.getPassword(), createUserRequestModel.getPin());
        Mockito.when(userRepository.findByPin(createUserRequestModel.getPin()))
                .thenReturn(Optional.of(model));


        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            userService.saveUser(createUserRequestModel);
        });
    }
}