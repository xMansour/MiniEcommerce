package us.exequt.ecommerce.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.exequt.ecommerce.order.domain.Order;
import us.exequt.ecommerce.order.domain.OrderStatus;
import us.exequt.ecommerce.order.exception.IllegalOrderStateException;
import us.exequt.ecommerce.order.mapper.OrderToOrderResponseMapper;
import us.exequt.ecommerce.order.mapper.OrderToPaymentAttemptRequestMapper;
import us.exequt.ecommerce.payment.PaymentFacade;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentFacade paymentFacade;
    @Mock
    OrderToPaymentAttemptRequestMapper orderToPaymentAttemptRequestMapper;
    @Mock
    OrderToOrderResponseMapper orderToOrderResponseMapper;

    @InjectMocks
    OrderService orderService;

    @Test
    void payForOrderThrowsWhenAlreadyPaid() {
        Order order = Order.builder().status(OrderStatus.PAID).build();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.payForOrder(order.getId()))
                .isInstanceOf(IllegalOrderStateException.class);
    }

    @Test
    void payForOrderThrowsWhenPaymentAlreadyInProgress() {
        Order order = Order.builder().status(OrderStatus.CREATED).build();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(paymentFacade.paymentAttemptAlreadyInProgress(order.getId())).thenReturn(true);

        assertThatThrownBy(() -> orderService.payForOrder(order.getId()))
                .isInstanceOf(IllegalOrderStateException.class);
    }

    @Test
    void payForOrderAllowsRetryWhenPaymentFailed() {
        Order order = Order.builder().status(OrderStatus.PAYMENT_FAILED).build();
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(paymentFacade.paymentAttemptAlreadyInProgress(order.getId())).thenReturn(false);
        when(orderRepository.save(any())).thenReturn(order);

        assertThatNoException().isThrownBy(() -> orderService.payForOrder(order.getId()));
    }
}
