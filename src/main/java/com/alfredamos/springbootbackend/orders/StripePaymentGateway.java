package com.alfredamos.springbootbackend.orders;

import com.alfredamos.springbootbackend.exceptions.PaymentException;
import com.alfredamos.springbootbackend.orderDetail.OrderDetail;
import com.alfredamos.springbootbackend.orders.dto.WebhookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway{
    private final StripeConfig stripeConfig;
    //private final OrderRepository orderRepository;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        System.out.println("In stripe-payment-gateway, website : " + stripeConfig.getWebsiteUrl());
        System.out.println("In stripe-payment-gateway, order : " + order);
        try {
            var builder = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl( stripeConfig.getWebsiteUrl()+ "/checkout-successUrl?orderId=" + order.getId())
                    .setCancelUrl(stripeConfig.getWebsiteUrl() + "/checkout-cancelUrl")
                    .putMetadata("order_id", order.getId().toString());

            order.getOrderDetails().forEach(item -> {
                var unitPrice = BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(100));
                System.out.println("In stripe-payment-gateway, unitPrice : " + unitPrice);
                var lineItem = createLineItem(item, unitPrice);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());

            return new CheckoutSession(session.getUrl());

        } catch (StripeException ex) {
            throw new PaymentException(ex.getMessage());
        }

    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try{
            var payload = request.getPayload();
            var signature = request.getHeaders().get("Stripe-Signature");

            var event = Webhook.constructEvent(payload, signature, stripeConfig.getWebhookSecretKey());

            switch (event.getType()){
                case "payment_intent.succeeded" -> {

                    return Optional.of(new PaymentResult(extractOrderId(event), Status.Paid));

                }

                case "payment_intent.payment_failed" -> {
                    return Optional.of(new PaymentResult(extractOrderId(event), Status.Failed));
                }

            };

            return Optional.empty();

        } catch (SignatureVerificationException ex) {
            throw new PaymentException("Invalid signature!");
        }
    }

    private UUID extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(() -> new PaymentException("Could not deserialize Stripe event. Check the SDK and Stripe version!"));

        var paymentIntent = (PaymentIntent) stripeObject;

        return UUID.fromString(paymentIntent.getMetadata().get("order_id"));
    }


    private SessionCreateParams.LineItem createLineItem(OrderDetail item, BigDecimal unitPrice) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                        createPriceData(item, unitPrice)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderDetail item, BigDecimal unitPrice) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(unitPrice)
                .setProductData(
                        createProductData(item)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderDetail item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getName()).build();
    }
}

