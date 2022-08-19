package az.company.unitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateUserRequestModel {
    @NotBlank(message = "FullName is mandatory")
    private String fullName;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Pin is mandatory")
    private String pin;
}
