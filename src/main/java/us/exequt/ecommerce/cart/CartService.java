package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;
import us.exequt.ecommerce.cart.dto.CartResponse;
import us.exequt.ecommerce.cart.dto.UpdateCartItemRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService implements CartFacade {
    private final CartRepository cartRepository;
    private final CartEntityToDtoMapper cartEntityToDtoMapper;
    private final CartItemDtoToEntityMapper cartItemDtoToEntityMapper;

    @Override
    public CartResponse addItemToCart(UUID cartId, AddCartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        CartItem cartItem = cartItemDtoToEntityMapper.apply(request);
        cart.addItem(cartItem);
        Cart updatedCart = cartRepository.save(cart);

        return cartEntityToDtoMapper.apply(updatedCart);
    }

    @Override
    public CartResponse updateItemInCart(UUID cartId, UUID itemId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        if (cart.getStatus().equals(CartStatus.INACTIVE))
            throw new CartLockedException("Cart with id: " + cartId + " is locked and cannot be modified");

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Item not found with id: " + itemId));

        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(request.getPrice());
        cartItem.setProductId(request.getProductId());
        Cart updatedCart = cartRepository.save(cart);

        return cartEntityToDtoMapper.apply(updatedCart);
    }

    @Override
    public void lockCart(UUID cartId) {

    }

    @Override
    public CartResponse create() {
        return cartEntityToDtoMapper.apply(cartRepository.save(new Cart()));
    }

    @Override
    public CartResponse getById(UUID id) {
        return null;
    }

    @Override
    public CartResponse update(UUID id, CartResponse dto) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
