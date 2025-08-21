package com.alfredamos.springbootbackend.orderDetail;

import java.util.UUID;

import com.alfredamos.springbootbackend.menuItems.Menu;
import com.alfredamos.springbootbackend.orders.Order;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="order-details")
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID  id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


}

