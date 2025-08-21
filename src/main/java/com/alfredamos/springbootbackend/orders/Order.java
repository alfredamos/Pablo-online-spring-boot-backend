package com.alfredamos.springbootbackend.orders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alfredamos.springbootbackend.orderDetail.OrderDetail;
import com.alfredamos.springbootbackend.users.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column
    private LocalDate orderDate;

    @Column
    private LocalDate deliveryDate;

    @Column
    private LocalDate shippingDate;

    @Column
    private String paymentId;

    @Column
    private Boolean isShipped;

    @Column
    private Boolean isDelivered;

    @Column
    private Boolean isPending;

    @Column
    private Integer totalQuantity;

    @Column
    private Double totalPrice;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Order setNewOrder(User user){
        this.setUser(user);

        var totalQuantity = this.totalQuantity();
        var totalPrice = this.totalPrice();

        var paymentId = this.getPaymentId() == null ?   UUID.randomUUID().toString() : this.getPaymentId();

        this.setOrderDate(LocalDate.now());
        this.setTotalPrice(totalPrice);
        this.setTotalQuantity(totalQuantity);
        this.setIsPending(true);
        this.setPaymentId(paymentId);
        this.setStatus(Status.Pending);

        this.setIsDelivered(false);
        this.setIsShipped(false);
        this.setOrderDetails(orderDetails);


        return this;
    }

    public Order deliveryInfo(){
        //----> Update the order delivery info.
        this.setIsDelivered(true); //----> Order shipped.
        this.setDeliveryDate(LocalDate.now()); //----> Order shipping date.
        this.setStatus(Status.Delivered); //----> Order status.
        //----> Return the updated order
        return this;
    }

    public Order shippingInfo(){
        //----> Update the order shipping info.
        this.setIsShipped(true); //----> Order shipped.
        this.setIsPending(false); //----> Order no longer pending.
        this.setShippingDate(LocalDate.now()); //----> Order shipping date.
        this.setStatus(Status.Shipped); //----> Order status.

        //----> Return the updated order.
        return this;
    }

    public void clearOrderDetails(){
        this.orderDetails.clear();
    }

    private Integer totalQuantity(){
        return this.orderDetails.stream().map(OrderDetail::getQuantity).reduce(0, Integer::sum);
    }

    private Double totalPrice(){
        return this.orderDetails.stream().map(cart -> cart.getPrice() * cart.getQuantity()).reduce(0.0, Double::sum);
    }

}
