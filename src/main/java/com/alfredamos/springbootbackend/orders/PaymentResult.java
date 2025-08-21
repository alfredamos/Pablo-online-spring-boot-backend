package com.alfredamos.springbootbackend.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private UUID orderId;
    private Status paymentStatus;
}

