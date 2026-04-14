package us.exequt.ecommerce.cart;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.cart.dto.CartItemResponse;

import java.util.function.Function;

@Component
public class CartItemEntityToDtoMapper implements Function<CartItem, CartItemResponse> {

    @Override
    public CartItemResponse apply(CartItem cartItem) {
        return CartItemResponse.builder()
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }
}
