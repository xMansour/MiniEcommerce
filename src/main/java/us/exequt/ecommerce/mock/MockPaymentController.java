package us.exequt.ecommerce.mock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.UUID;

@Tag(name = "Mock Payment Provider", description = "Simulates an external payment provider.")
@RestController
@RequestMapping("/mock-payments")
@RequiredArgsConstructor
public class MockPaymentController {
    private final MockPaymentService mockPaymentService;

    @Operation(summary = "Confirm a payment attempt", description = "Triggers a SUCCESS webhook for the given attempt.")
    @PostMapping("/{attemptId}/confirm")
    public ResponseEntity<RestResponse<Void>> confirm(@PathVariable UUID attemptId) {
        mockPaymentService.confirmPayment(attemptId);
        return ResponseEntity.ok(RestResponse.success("Payment was accepted successfully", null));
    }

    @Operation(summary = "Fail a payment attempt", description = "Triggers a FAILED webhook for the given attempt.")
    @PostMapping("/{attemptId}/fail")
    public ResponseEntity<RestResponse<Void>> fail(@PathVariable UUID attemptId) {
        mockPaymentService.rejectPayment(attemptId);
        return ResponseEntity.ok(RestResponse.success("Payment was rejected successfully", null));
    }
}
