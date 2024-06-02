package com.kakaobank.order.order;

import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.order.dto.CartResponse;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.dto.OrderRequest;
import com.kakaobank.order.common.util.UserContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;



    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/cart")
    public List<CartResponse> addCart(UserContext context, @RequestBody AddCartRequest request) {
        this.orderService.addCart(context, request);
        return this.orderService.getCartList(context.getUuid());
    }

    @GetMapping("/trouble")
    public List<String> test() {
        return List.of("WHAT??????");
    }

    @GetMapping("/cart")
    public List<CartResponse> getCart(UserContext context) {
        return this.orderService.getCartList(context.getUuid());
    }

    @DeleteMapping("/cart")
    public List<CartResponse> deleteCartItems(UserContext context, @RequestBody DeleteCartItemRequest request) {
        return this.orderService.deleteCartItems(context, request);
    }

    @PostMapping("/order")
    public Order order(UserContext context, @RequestBody List<OrderRequest> requests) {
        return this.orderService.makeOrder(context, requests);
    }

    @GetMapping("/order/history")
    public List<Order> orderHistory(UserContext context) {
        return this.orderService.getOrderHistory(context.getUuid());
    }

    @DeleteMapping("/order/{orderId}")
    public Order cancelOrder(UserContext context, @PathVariable String orderId){
        return this.orderService.cancelOrder(context, orderId);
    }

}
