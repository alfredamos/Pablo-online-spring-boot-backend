package com.alfredamos.springbootbackend.orders;

import com.alfredamos.springbootbackend.exceptions.BadRequestException;
import com.alfredamos.springbootbackend.exceptions.ForbiddenException;
import com.alfredamos.springbootbackend.exceptions.NotFoundException;
import com.alfredamos.springbootbackend.exceptions.PaymentException;
import com.alfredamos.springbootbackend.menuItems.MenuRepository;
import com.alfredamos.springbootbackend.orderDetail.OrderDetail;
import com.alfredamos.springbootbackend.orderDetail.OrderDetailMapper;
import com.alfredamos.springbootbackend.orderDetail.OrderDetailRepository;
import com.alfredamos.springbootbackend.orders.dto.WebhookRequest;
import com.alfredamos.springbootbackend.users.UserRepository;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import com.alfredamos.springbootbackend.orders.dto.OrderDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final UserRepository userRepository;
    private final MenuRepository menuItemRepository;
    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutSession checkoutOrder(OrderDto orderDto) {
        System.out.println("At point 2, checkoutOrder, orderDto = " + orderDto);
        if (orderDto.getOrderDetailsDto() != null) {
            //----> Mapped orderDto to order.
            var order = this.orderMapper.toEntity(orderDto);

            //----> Get the cartItems.
            var orderDetails = this.setOrderDetailList(orderDto);

            //----> Get the user creating the order.
            var user = this.userRepository.findById(orderDto.getUserId()).orElse(null);

            //----> Attach the order-details to the order.
            order.setOrderDetails(orderDetails);

            var adjustedOrder = order.setNewOrder(user);

            //----> Save the cart-items into the cart-items table.
            orderDetails.forEach(orderDetail -> {
                orderDetail.setOrder(adjustedOrder);
            });
            var newOrder = this.orderRepository.save(adjustedOrder);

            try{
                //----> Checkout order-payment
                var session = paymentGateway.createCheckoutSession(newOrder);

                //----> Send back checkout order url.
                return new CheckoutSession(session.getCheckoutUrl());
            } catch (PaymentException ex) {
                orderRepository.delete(newOrder);
                throw new PaymentException(ex.getMessage());
            }

        }

        return new CheckoutSession();
    }


    public ResponseMessage deleteOrderById(UUID id, boolean canDeleteAndView){
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("User doesn't have permission to remove this order");
        }

        //----> Delete the order with given id from the database.
        this.orderRepository.deleteById(id);

        return new ResponseMessage("Success", "Order has been deleted successfully!", 200);

    }

    @Transactional
    public ResponseMessage deleteOrdersByUserId(UUID userId, boolean canDeleteAndView){
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("User doesn't have permission to remove this order");
        }

        //----> Get the user.
        var user = this.userRepository.findById(userId).orElseThrow();
        System.out.println("In delete-order-by-userId, user : " + user);

        //----> Delete all orders associated with this user.
        this.orderRepository.deleteOrdersByUser(user);

        return new ResponseMessage("Success", "All orders associated with this user are deleted!", 200);
    }


    public ResponseMessage deleteAllOrders(){
        //----> Delete all associated cart-items.
        this.orderDetailRepository.deleteAllInBatch();

        //----> Delete all orders.
        this.orderRepository.deleteAllInBatch();

        return new ResponseMessage("Success", "All orders associated with this user are deleted!", 200);
    }

    public OrderDto deliveredOrder(UUID orderId){
        //----> Check for existence of order.
        this.checkForOrderExistence(orderId);

        //----> Get the order.
        var order = this.orderRepository.findById(orderId).orElseThrow(() ->  new NotFoundException("This order is not found the database!"));

        if (!order.getIsShipped()) {
            throw new BadRequestException("Order must be shipped before delivery, please ship the order!");
        }

        //----> Update the order delivery info.
        var deliveredOrder = order.deliveryInfo();

        //----> Update the order delivery info in the database.

        var editedOrder = this.orderRepository.save(deliveredOrder);

        return this.attachCartItemsDtoToOrderDto(editedOrder);
    }

    public List<OrderDto> getAllOrdersByUserById(UUID userId, boolean canDeleteAndView){
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("you don't have permission to view these orders");
        }

        //----> Get the user associated with the orders.
        var user = this.userRepository.findById(userId).orElseThrow();

        var orders = this.orderRepository.findOrdersByUser(user);

        return orders.stream().map(this::attachCartItemsDtoToOrderDto).toList();
    }

    public List<OrderDto> getAllOrders() {
        var orders = this.orderRepository.findAll();

        return orders.stream().map(this::attachCartItemsDtoToOrderDto).toList();
    }

    public OrderDto getOrderById(UUID id, boolean canDeleteAndView){
        //----> Check for ownership or admin privilege.
        if (!canDeleteAndView) {
            throw new ForbiddenException("you don't have permission to view these orders");
        }

        var order = this.orderRepository.findById(id).orElse(null);

        return this.attachCartItemsDtoToOrderDto(order);

    }

    public OrderDto shippedOrder(UUID orderId){
        //----> Check for existence of order.
        this.checkForOrderExistence(orderId);

        //----> Get the order.
        var order = this.orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("This order is not found the database!"));

        //----> Update the shipping information.
        var shippedOrder = order.shippingInfo();

        //----> Update the order in the database.

        var editedOrder = this.orderRepository.save(shippedOrder);

        return this.attachCartItemsDtoToOrderDto(editedOrder);

    }

    public void handleWebhook(WebhookRequest request){
        paymentGateway.parseWebhookRequest(request).ifPresent(paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();

            order.setStatus(paymentResult.getPaymentStatus());

            orderRepository.save(order);
        });

    }

    private void checkForOrderExistence(UUID id){
        var exist = this.orderRepository.existsById(id);

        //----> Check for existence of order.
        if (!exist){
            throw new NotFoundException("Order does not exist!");
        }
    }

    private List<OrderDetail> setOrderDetailList(OrderDto orderDto){
        //----> Map the cart-items-dto to cart-items with the associated pizzas.
        return orderDto.getOrderDetailsDto().stream().map(orderDetailDto -> {
            var orderDetail = this.orderDetailMapper.toEntity(orderDetailDto);

            var menuItem = this.menuItemRepository.findById(orderDetailDto.getMenuId()).orElseThrow();

            orderDetail.setMenu(menuItem);
            return orderDetail;


        }).toList();

    }

    public Order getOneOrder(UUID id){
        //----> Get the order with given id.
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    private OrderDto attachCartItemsDtoToOrderDto(Order order){

        var orderDetails = this.orderDetailRepository.findAllByOrder(order);

        //----> Attach the newly created order to the cart-items.n
        order.setOrderDetails(orderDetails);

        //----> Map order to orderDto.
        var orderDto = this.orderMapper.toDTO(order);

        //----> Set user-id.
        orderDto.setUserId(order.getUser().getId());

        //----> Set order-details.
        orderDto.setOrderDetailsDto(this.orderDetailMapper.toDTOList(orderDetails));

        return orderDto;
    }

}

