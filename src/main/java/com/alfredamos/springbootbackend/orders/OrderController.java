package com.alfredamos.springbootbackend.orders;


import com.alfredamos.springbootbackend.auth.OwnerCheck;
import com.alfredamos.springbootbackend.orders.dto.OrderDto;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OwnerCheck  ownerCheck;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutSession> checkoutOrder(@Valid @RequestBody OrderDto orderDto){
        System.out.println("At point 1, checkoutOrder, orderDto = " + orderDto);
        //----> Checkout your order.
        var orderCheckoutsession = orderService.checkoutOrder(orderDto);

        //----> send back the response.
        return ResponseEntity.ok().body(orderCheckoutsession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteOrderById(@PathVariable(name = "id") UUID id) {
        //----> Get the order with given id.
        var order = orderService.getOneOrder(id);

        //----> Delete the order with given id.
        var response = orderService.deleteOrderById(id, ownerCheck.userIdMatchesContextUserId(order.getUser().getId()));

        //----> send back the response.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete-all-orders-by-user-id/{userId}")
    public ResponseEntity<ResponseMessage> deleteOrdersByUserId(@PathVariable(name = "userId") UUID userId) {
        //----> Delete the order with given id.
        var response = orderService.deleteOrdersByUserId(userId, ownerCheck.userIdMatchesContextUserId(userId));

        //----> send back the response.
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "id") UUID id) {
        //----> Get the order with given id.
        var order = orderService.getOneOrder(id);


        //----> Delete the order with given id.
        var response = orderService.getOrderById(id, ownerCheck.userIdMatchesContextUserId(order.getUser().getId()));

        //----> send back the response.
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders-by-user-id/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserId(@PathVariable(name = "userId") UUID userId) {
        //----> Delete the order with given id.
        var response = orderService.getAllOrdersByUserById(userId, ownerCheck.userIdMatchesContextUserId(userId));

        //----> send back the response.
        return ResponseEntity.ok().body(response);
    }


}
