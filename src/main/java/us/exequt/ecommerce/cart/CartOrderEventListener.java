package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.OrderCancelledEvent;

@Component
@RequiredArgsConstructor
public class CartOrderEventListener {
    private final CartFacade cartFacade;

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        cartFacade.unlockCart(event.getCartId());
    }
}
