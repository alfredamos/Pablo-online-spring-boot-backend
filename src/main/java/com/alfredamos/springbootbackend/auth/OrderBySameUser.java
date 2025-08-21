package com.alfredamos.springbootbackend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

@AllArgsConstructor
@Data
public class OrderBySameUser {
    private SpringDataJaxb.OrderDto orderDto;
    private boolean isOwner;
}

