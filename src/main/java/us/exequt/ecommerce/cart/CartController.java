package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;
import us.exequt.ecommerce.cart.dto.CartResponse;
import us.exequt.ecommerce.cart.dto.UpdateCartItemRequest;
import us.exequt.ecommerce.shared.RestResponse;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartFacade cartService;

    @PostMapping
    public ResponseEntity<RestResponse<CartResponse>> createCart() {
        CartResponse cart = cartService.create();
        return ResponseEntity
                .created(URI.create("/carts/" + cart.getId()))
                .body(RestResponse.success("Cart created successfully", cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<RestResponse<CartResponse>> addItemToCart(@PathVariable UUID cartId, AddCartItemRequest request) {
        CartResponse updatedCart = cartService.addItemToCart(cartId, request);
        return ResponseEntity.ok(RestResponse.success("Item added to cart successfully", updatedCart));
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<RestResponse<CartResponse>> updateItemInCart(@PathVariable UUID cartId, @PathVariable UUID itemId, UpdateCartItemRequest request) {
        CartResponse updatedCart = cartService.updateItemInCart(cartId, itemId, request);
        return ResponseEntity.ok(RestResponse.success("Item with id: " + itemId + " was updated successfully", updatedCart));
    }
}
