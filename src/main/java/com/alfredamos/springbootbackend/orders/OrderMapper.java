package com.alfredamos.springbootbackend.orders;

import com.alfredamos.springbootbackend.orders.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);

    OrderDto toDTO(Order order);

    List<OrderDto> toDTOList(List<Order> orders);
}


