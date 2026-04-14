package us.exequt.ecommerce.cart;

import us.exequt.ecommerce.cart.dto.AddCartItemRequest;
import us.exequt.ecommerce.cart.dto.CartResponse;
import us.exequt.ecommerce.cart.dto.UpdateCartItemRequest;
import us.exequt.ecommerce.shared.base.BaseService;

import java.util.UUID;

public interface CartFacade extends BaseService<CartResponse, UUID> {
    CartResponse addItemToCart(UUID cartId, AddCartItemRequest request);
    CartResponse updateItemInCart(UUID cartId, UUID itemId, UpdateCartItemRequest request);
    void lockCart(UUID cartId); //TODO:: this should be called when we checkout
}
