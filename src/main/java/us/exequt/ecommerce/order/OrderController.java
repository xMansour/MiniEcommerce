package us.exequt.ecommerce.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.order.dto.OrderResponse;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Order")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderFacade orderService;

    @Operation(summary = "Get all orders")
    @GetMapping
    public ResponseEntity<RestResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAll();
        return ResponseEntity.ok(RestResponse.success("Orders retrieved successfully", orders));
    }

    @Operation(summary = "Get an order by ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<RestResponse<OrderResponse>> getOrderById(@PathVariable UUID orderId) {
        OrderResponse order = orderService.getById(orderId);
        return ResponseEntity.ok(RestResponse.success("Order with id: " + orderId + " was retrieved successfully", order));
    }

    @Operation(summary = "Cancel an order", description = "Valid from CREATED, PENDING_PAYMENT, or PAYMENT_FAILED. Unlocks the cart.")
    @PostMapping("/{orderId}")
    public ResponseEntity<RestResponse<OrderResponse>> cancelOrder(@PathVariable UUID orderId) {
        OrderResponse order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(RestResponse.success("Order with id: " + orderId + " was canceled successfully", order));
    }

    @Operation(summary = "Start payment for an order", description = "Creates a payment attempt and moves the order to PENDING_PAYMENT. Rejected if a PENDING attempt already exists.")
    @PostMapping("/{orderId}/payment/start")
    public ResponseEntity<RestResponse<OrderResponse>> payForOrder(@PathVariable UUID orderId) {
        OrderResponse order = orderService.payForOrder(orderId);
        return ResponseEntity.ok(RestResponse.success("Payment for order with id: " + orderId + " was initiated successfully", order));
    }
}
