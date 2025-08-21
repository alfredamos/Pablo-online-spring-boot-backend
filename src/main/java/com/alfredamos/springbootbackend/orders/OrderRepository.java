package com.alfredamos.springbootbackend.orders;

import com.alfredamos.springbootbackend.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    void deleteOrdersByUser(User user);
    List<Order> findOrdersByUser(User user);
}
