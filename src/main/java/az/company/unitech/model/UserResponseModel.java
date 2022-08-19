package az.company.unitech.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserResponseModel {
    private Long id;
    private String fullName;
    private String pin;
}
