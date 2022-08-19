package az.company.unitech.controller;

import az.company.unitech.entity.User;
import az.company.unitech.model.AuthenticationRequestModel;
import az.company.unitech.model.AuthenticationResponseModel;
import az.company.unitech.model.CreateUserRequestModel;
import az.company.unitech.model.UserResponseModel;
import az.company.unitech.service.MyUserDetailsService;
import az.company.unitech.service.UserService;
import az.company.unitech.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseModel> register(@RequestBody @Valid CreateUserRequestModel model) {
        User user = userService.saveUser(model);
        UserResponseModel responseModel = new UserResponseModel(user.getId(), user.getFullName(), user.getPin());
        return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid AuthenticationRequestModel authenticationRequestModel) throws Exception{

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestModel.getPin(),
                        authenticationRequestModel.getPassword()));


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestModel.getPin());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseModel(jwt));
    }

}
