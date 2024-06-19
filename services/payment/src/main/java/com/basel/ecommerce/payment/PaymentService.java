package com.basel.ecommerce.payment;

import com.basel.ecommerce.notification.NotificationProducer;
import com.basel.ecommerce.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));

        PaymentNotificationRequest paymentNotificationRequest = new PaymentNotificationRequest(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customer().firstname(),
                paymentRequest.customer().lastname(),
                paymentRequest.customer().email()
        );
        notificationProducer.sendNotification(paymentNotificationRequest);

        return payment.getId();
    }

}
