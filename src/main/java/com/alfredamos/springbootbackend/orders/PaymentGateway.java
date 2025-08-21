package com.alfredamos.springbootbackend.orders;

import com.alfredamos.springbootbackend.orders.dto.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
