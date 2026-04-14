package us.exequt.ecommerce.cart;

public class IllegalCartStateException extends RuntimeException {
    public IllegalCartStateException(String message) {
        super(message);
    }
}
