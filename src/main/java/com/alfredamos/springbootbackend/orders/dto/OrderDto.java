package com.alfredamos.springbootbackend.orders.dto;

import com.alfredamos.springbootbackend.orderDetail.OrderDetailDto;
import com.alfredamos.springbootbackend.orders.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private LocalDate orderDate;

    private LocalDate deliveryDate;

    private LocalDate shippingDate;

    private String paymentId;

    private Boolean isShipped;

    private Boolean isDelivered;

    private Boolean isPending;

    private Integer totalQuantity;

    private Double totalPrice;

    private Status status;

    private List<OrderDetailDto> orderDetailsDto;

    private UUID userId;

}

