package us.exequt.ecommerce.cart;

public class CartLockedException extends RuntimeException {
    public CartLockedException(String message) {
        super(message);
    }
}
