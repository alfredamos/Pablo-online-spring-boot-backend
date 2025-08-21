package com.alfredamos.springbootbackend.users;

import java.util.List;
import java.util.UUID;

import com.alfredamos.springbootbackend.menuItems.Menu;
import com.alfredamos.springbootbackend.orders.Order;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String password;

    @Column()
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @ToString.Exclude
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Menu> menus;

}

