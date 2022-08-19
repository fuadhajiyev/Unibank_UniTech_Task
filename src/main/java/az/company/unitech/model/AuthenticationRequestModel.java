package az.company.unitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Slf4j
@AllArgsConstructor
@Data
public class AuthenticationRequestModel {
    @NotBlank(message = "pin is mandatory")
    private String pin;
    @NotBlank(message = "password is mandatory")
    private String password;
}
