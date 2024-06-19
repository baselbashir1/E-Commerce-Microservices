package com.basel.ecommerce.order;

import com.basel.ecommerce.customer.CustomerClient;
import com.basel.ecommerce.customer.CustomerResponse;
import com.basel.ecommerce.exception.BusinessException;
import com.basel.ecommerce.kafka.OrderConfirmation;
import com.basel.ecommerce.kafka.OrderProducer;
import com.basel.ecommerce.orderline.OrderLineRequest;
import com.basel.ecommerce.orderline.OrderLineService;
import com.basel.ecommerce.payment.PaymentClient;
import com.basel.ecommerce.payment.PaymentRequest;
import com.basel.ecommerce.product.ProductClient;
import com.basel.ecommerce.product.PurchaseRequest;
import com.basel.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest orderRequest) {
        CustomerResponse customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID."));

        List<PurchaseResponse> purchasedProducts = productClient.purchaseProducts(orderRequest.products());
        Order order = orderRepository.save(orderMapper.toOrder(orderRequest));

        for (PurchaseRequest purchaseRequest : orderRequest.products()) {
            OrderLineRequest orderLineRequest = new OrderLineRequest(null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity());
            orderLineService.saveOrderLine(orderLineRequest);
        }

        PaymentRequest paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                orderRequest.id(),
                orderRequest.reference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        OrderConfirmation orderConfirmation = new OrderConfirmation(orderRequest.reference(), orderRequest.amount(), orderRequest.paymentMethod(), customer, purchasedProducts);
        orderProducer.sendOrderConfirmation(orderConfirmation);

        return order.getId();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }

}
