package az.company.unitech.model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ValidationErrorResponse {
    //General error message about nature of error
    private String message;

    //Specific errors in API request processing
    private List<String> details;
}
