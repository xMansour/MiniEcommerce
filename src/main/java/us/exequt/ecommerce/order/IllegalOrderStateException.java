package us.exequt.ecommerce.order;

public class IllegalOrderStateException extends RuntimeException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}
