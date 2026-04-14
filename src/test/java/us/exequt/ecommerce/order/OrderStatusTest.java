package us.exequt.ecommerce.order;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import us.exequt.ecommerce.order.domain.OrderStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @ParameterizedTest(name = "{0} -> {1} should be allowed")
    @MethodSource("validTransitions")
    void allowsValidTransitions(OrderStatus from, OrderStatus to) {
        assertThat(from.canTransitionTo(to)).isTrue();
    }

    @ParameterizedTest(name = "{0} -> {1} should be rejected")
    @MethodSource("invalidTransitions")
    void rejectsInvalidTransitions(OrderStatus from, OrderStatus to) {
        assertThat(from.canTransitionTo(to)).isFalse();
    }

    static Stream<Arguments> validTransitions() {
        return Stream.of(
                Arguments.of(OrderStatus.CREATED,         OrderStatus.PENDING_PAYMENT),
                Arguments.of(OrderStatus.CREATED,         OrderStatus.CANCELED),
                Arguments.of(OrderStatus.PENDING_PAYMENT, OrderStatus.PAID),
                Arguments.of(OrderStatus.PENDING_PAYMENT, OrderStatus.PAYMENT_FAILED),
                Arguments.of(OrderStatus.PENDING_PAYMENT, OrderStatus.CANCELED),
                Arguments.of(OrderStatus.PAYMENT_FAILED,  OrderStatus.PENDING_PAYMENT),
                Arguments.of(OrderStatus.PAYMENT_FAILED,  OrderStatus.CANCELED)
        );
    }

    static Stream<Arguments> invalidTransitions() {
        return Stream.of(
                Arguments.of(OrderStatus.CREATED,         OrderStatus.PAID),
                Arguments.of(OrderStatus.CREATED,         OrderStatus.PAYMENT_FAILED),
                Arguments.of(OrderStatus.PENDING_PAYMENT, OrderStatus.CREATED),
                Arguments.of(OrderStatus.PAYMENT_FAILED,  OrderStatus.PAID),
                Arguments.of(OrderStatus.PAYMENT_FAILED,  OrderStatus.CREATED),
                Arguments.of(OrderStatus.PAID,            OrderStatus.PENDING_PAYMENT),
                Arguments.of(OrderStatus.PAID,            OrderStatus.PAYMENT_FAILED),
                Arguments.of(OrderStatus.PAID,            OrderStatus.CANCELED),
                Arguments.of(OrderStatus.CANCELED,        OrderStatus.CREATED),
                Arguments.of(OrderStatus.CANCELED,        OrderStatus.PENDING_PAYMENT),
                Arguments.of(OrderStatus.CANCELED,        OrderStatus.PAID)
        );
    }
}
