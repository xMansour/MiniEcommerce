package us.exequt.ecommerce.order;

public class OrderAlreadyCanceledException extends RuntimeException {
    public OrderAlreadyCanceledException(String message) {
        super(message);
    }
}
