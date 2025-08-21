package com.alfredamos.springbootbackend.menuItems;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alfredamos.springbootbackend.orderDetail.OrderDetail;
import com.alfredamos.springbootbackend.users.User;
import jakarta.persistence.*;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "menus")
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "menu")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

