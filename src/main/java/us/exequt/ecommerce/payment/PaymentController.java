package us.exequt.ecommerce.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Payment")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFacade paymentService;

    @Operation(summary = "Payment webhook", description = "Receives the payment result from the provider. Duplicate calls with the same attemptId are rejected with 400.")
    @PostMapping("/webhook")
    public ResponseEntity<RestResponse<PaymentAttemptResponse>> handlePaymentAttemptResult(@RequestBody PaymentAttemptResultRequest request) {
        PaymentAttemptResponse paymentAttempt = paymentService.processPaymentAttemptResult(request);
        return ResponseEntity.ok(RestResponse.success("Payment attempt for order with id: " + paymentAttempt.getOrderId() + " was processed successfully", paymentAttempt));
    }

    @Operation(summary = "Get all payment attempts")
    @GetMapping
    public ResponseEntity<RestResponse<List<PaymentAttemptResponse>>> getAllPaymentAttempts() {
        List<PaymentAttemptResponse> payments = paymentService.getAll();
        return ResponseEntity.ok(RestResponse.success("Payments retrieved successfully", payments));
    }

    @Operation(summary = "Get a payment attempt by ID")
    @GetMapping("/{paymentAttemptId}")
    public ResponseEntity<RestResponse<PaymentAttemptResponse>> getOrderById(@PathVariable UUID paymentAttemptId) {
        PaymentAttemptResponse payment = paymentService.getById(paymentAttemptId);
        return ResponseEntity.ok(RestResponse.success("Payment attempt with id: " + paymentAttemptId + " retrieved successfully", payment));
    }
}
