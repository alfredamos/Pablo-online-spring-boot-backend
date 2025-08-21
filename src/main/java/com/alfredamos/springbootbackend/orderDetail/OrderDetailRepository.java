package com.alfredamos.springbootbackend.orderDetail;

import com.alfredamos.springbootbackend.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
        List<OrderDetail> findAllByOrder(Order order);
}
