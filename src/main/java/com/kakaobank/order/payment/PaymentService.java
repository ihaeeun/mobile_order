package com.kakaobank.order.payment;

import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.Payment;
import com.kakaobank.order.payment.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Random;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final Random random;

    PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.random = new Random();
    }

    public PaymentResponse makePayment(Order order) throws InterruptedException {
        Thread.sleep((long) (Math.random() * 1000));
        if (random.nextInt() % 100 == 1) {
            throw new PaymentServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment.");
        }
        var payment = paymentRepository.save(new Payment(order.getId()));
        return new PaymentResponse(true, payment);
    }

    public PaymentResponse cancelPayment(Order order) throws InterruptedException{
        Thread.sleep((long) (Math.random() * 1000));
        if (random.nextInt() % 100 == 1) {
            throw new PaymentServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail cancel payment.");
        }
        var payment = paymentRepository.findByOrderId(order.getId());
        payment.setCancellation(true);
        paymentRepository.save(payment);
        return new PaymentResponse(true, payment);
    }

    public static class PaymentServiceException extends HttpStatusCodeException {
        public PaymentServiceException(HttpStatusCode statusCode, String statusText) {
            super(statusCode, statusText);
        }
    }
}
