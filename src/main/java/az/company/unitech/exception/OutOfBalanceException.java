package az.company.unitech.exception;

public class OutOfBalanceException extends RuntimeException {
    public OutOfBalanceException(String message) {
        super(message);
    }
}