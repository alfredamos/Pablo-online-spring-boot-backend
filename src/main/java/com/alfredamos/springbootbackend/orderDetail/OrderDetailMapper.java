package com.alfredamos.springbootbackend.orderDetail;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail toEntity(OrderDetailDto orderDetailDto);

    @Mapping(source = "menu.id", target = "menuId")
    OrderDetailDto toDTO(OrderDetail orderDetail);

    List<OrderDetailDto> toDTOList(List<OrderDetail> orderDetails);

    List<OrderDetail> toEntityList(List<OrderDetailDto> orderDetailDtos);
}
