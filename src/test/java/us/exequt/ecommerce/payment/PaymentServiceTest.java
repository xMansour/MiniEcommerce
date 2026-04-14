package us.exequt.ecommerce.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import us.exequt.ecommerce.payment.domain.PaymentAttempt;
import us.exequt.ecommerce.payment.domain.PaymentStatus;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;
import us.exequt.ecommerce.payment.event.PaymentAttemptEvent;
import us.exequt.ecommerce.payment.exception.IllegalPaymentAttemptStateException;
import us.exequt.ecommerce.payment.mapper.PaymentAttemptRequestToPaymentAttemptMapper;
import us.exequt.ecommerce.payment.mapper.PaymentAttemptToPaymentAttemptResponseMapper;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    PaymentProviderGateway paymentProviderGateway;
    @Mock
    PaymentAttemptToPaymentAttemptResponseMapper responseMapper;
    @Mock
    PaymentAttemptRequestToPaymentAttemptMapper requestMapper;

    @InjectMocks
    PaymentService paymentService;


    @Test
    void processPaymentAttemptResultThrowsOnDuplicateWebhookWhenAlreadySucceeded() {
        PaymentAttempt attempt = PaymentAttempt.builder()
                .orderId(UUID.randomUUID())
                .status(PaymentStatus.SUCCESS)
                .build();
        when(paymentRepository.findById(attempt.getId())).thenReturn(Optional.of(attempt));

        PaymentAttemptResultRequest request = PaymentAttemptResultRequest.builder()
                .attemptId(attempt.getId())
                .result(PaymentStatus.SUCCESS)
                .build();

        assertThatThrownBy(() -> paymentService.processPaymentAttemptResult(request))
                .isInstanceOf(IllegalPaymentAttemptStateException.class);
    }

    @Test
    void processPaymentAttemptResultThrowsOnDuplicateWebhookWhenAlreadyFailed() {
        PaymentAttempt attempt = PaymentAttempt.builder()
                .orderId(UUID.randomUUID())
                .status(PaymentStatus.FAILED)
                .build();
        when(paymentRepository.findById(attempt.getId())).thenReturn(Optional.of(attempt));

        PaymentAttemptResultRequest request = PaymentAttemptResultRequest.builder()
                .attemptId(attempt.getId())
                .result(PaymentStatus.FAILED)
                .build();

        assertThatThrownBy(() -> paymentService.processPaymentAttemptResult(request))
                .isInstanceOf(IllegalPaymentAttemptStateException.class);
    }

    @Test
    void processPaymentAttemptResultPublishesEventOnSuccess() {
        PaymentAttempt attempt = PaymentAttempt.builder()
                .status(PaymentStatus.PENDING)
                .orderId(UUID.randomUUID())
                .build();
        when(paymentRepository.findById(attempt.getId())).thenReturn(Optional.of(attempt));
        when(paymentRepository.save(any())).thenReturn(attempt);

        paymentService.processPaymentAttemptResult(PaymentAttemptResultRequest.builder()
                .attemptId(attempt.getId())
                .result(PaymentStatus.SUCCESS)
                .build());

        verify(eventPublisher).publishEvent(any(PaymentAttemptEvent.class));
    }
}
