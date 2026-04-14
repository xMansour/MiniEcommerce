package us.exequt.ecommerce.cart;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;

import java.util.function.Function;

@Component
public class CartItemDtoToEntityMapper implements Function<AddCartItemRequest, CartItem> {
    @Override
    public CartItem apply(AddCartItemRequest addCartItemRequest) {
        return CartItem.builder()
                .productId(addCartItemRequest.getProductId())
                .quantity(addCartItemRequest.getQuantity())
                .price(addCartItemRequest.getPrice())
                .build();
    }
}
