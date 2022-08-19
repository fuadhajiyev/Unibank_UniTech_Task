package az.company.unitech.exception;

public class DeactiveAccountException extends RuntimeException {

    public DeactiveAccountException(String message) {
        super(message);
    }
}
