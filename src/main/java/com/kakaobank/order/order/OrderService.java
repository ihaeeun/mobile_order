package com.kakaobank.order.order;

import com.kakaobank.order.common.aop.UserAction;
import com.kakaobank.order.common.entity.ActionType;
import com.kakaobank.order.common.entity.CartItem;
import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.OrderItem;
import com.kakaobank.order.common.entity.OrderStatus;
import com.kakaobank.order.common.entity.Product;
import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.order.dto.CartResponse;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.dto.OrderRequest;
import com.kakaobank.order.order.repository.CartItemRepository;
import com.kakaobank.order.order.repository.OrderItemRepository;
import com.kakaobank.order.order.repository.OrderRepository;
import com.kakaobank.order.payment.PaymentService;
import com.kakaobank.order.product.ProductService;
import com.kakaobank.order.common.util.UserContext;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    private final PaymentService paymentService;

    private final ProductService productService;

    OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository, PaymentService paymentService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @Transactional
    @UserAction(actionType = ActionType.ADD_CART)
    public List<CartResponse> addCart(UserContext context, AddCartRequest request) {
        var product = this.productService.getProductDetail(request.productId());

        if (product.isAvailable(request.quantity())) {
            var cartItem = this.cartItemRepository.findByUserIdAndProductId(context.getUuid(), request.productId());

            if (ObjectUtils.isEmpty(cartItem)) {
                var item = new CartItem(context.getUuid(), request.productId(), request.quantity());
                this.cartItemRepository.save(item);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
                this.cartItemRepository.save(cartItem);
            }

            return getCartList(context.getUuid());
        } else {
            throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Out of stock(Available : " + product.getStock() + ")");
        }
    }

    // todo join table
    public List<CartResponse> getCartList(String userId) {
        var cartItem = this.cartItemRepository.findByUserId(userId);
        return cartItem.stream().map(c -> {
            var product = this.productService.getProductDetail(c.getProductId());
            return new CartResponse(c.getId(), product.getId(), product.getName(), product.getPrice(), c.getQuantity(), product.isAvailable(c.getQuantity()));
        }).toList();

    }

    @Transactional
    @UserAction(actionType = ActionType.DELETE_CART)
    public List<CartResponse> deleteCartItems(UserContext context, DeleteCartItemRequest request) {
        this.cartItemRepository.deleteAllById(request.itemIds());
        return getCartList(context.getUuid());
    }

    public List<Order> getOrderHistory(String userId) {
        return this.orderRepository.findAllByUserId(userId);
    }

    // 재고가 없는 상품이 포함되어 있으면 전체 주문 불가
    @Transactional
    @UserAction(actionType = ActionType.MAKE_ORDER)
    public Order makeOrder(UserContext context, List<OrderRequest> requests) {

        List<OrderItem> orderItems = new LinkedList<>();
        List<Product> products = new LinkedList<>();
        var orderId = UUID.randomUUID().toString();

        for (OrderRequest orderRequest : requests) {
            // 재고 확인
            var product = this.productService.getProductDetail(orderRequest.productId());
            if (product.isAvailable(orderRequest.quantity())) {
                var orderItem = new OrderItem(orderId, product.getId(), orderRequest.quantity(), product.getPrice());
                orderItems.add(orderItem);
                products.add(product);
            } else {
                var message = new StringBuilder().append(product.getName()).append(" is unavailable. (Available: ").append(product.getStock()).append(")").toString();
                throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, message);
            }
        }

        var totalAmount = orderItems.stream().mapToInt((item) -> item.getPrice() * item.getQuantity()).sum();
        var order = new Order(orderId, context.getUuid(), totalAmount);

        try {
            // payment 호출
            var paymentResponse = this.paymentService.makePayment(order);

            if (paymentResponse.result()) {
                this.orderItemRepository.saveAll(orderItems);

                // 재고 줄임
                for (int i = 0; i < orderItems.size(); i++) {
                    products.get(i).updateStock(orderItems.get(i).getQuantity());
                }
                this.productService.updateStock(products);

                order.setOrderStatus(OrderStatus.PAID);
                return this.orderRepository.save(order);
            }
            throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment.");

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (HttpStatusCodeException ex) {
            throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment.");
        }
    }

    @Transactional
    @UserAction(actionType = ActionType.CANCEL_ORDER)
    public Order cancelOrder(UserContext userContext, String orderId) {
        var order = this.orderRepository.findByIdAndOrderStatus(orderId, OrderStatus.PAID);
        if (ObjectUtils.isEmpty(order)) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, "It is not valid order.");
        }

        try {
            var paymentResponse = this.paymentService.cancelPayment(order);
            List<Product> products = new ArrayList<>();

            order.setOrderStatus(OrderStatus.CANCELED);
            // 상품 재고 업데이트
            this.orderItemRepository.findAllByOrderId(orderId)
                    .forEach((item) -> {
                        var product = this.productService.getProductDetail(item.getProductId());
                        product.updateStock(item.getQuantity() * -1);
                        products.add(product);
                    });

            this.productService.updateStock(products);
            return this.orderRepository.save(order);
        } catch (InterruptedException ex) {
            throw new RuntimeException();
        } catch (HttpStatusCodeException ex) {
            throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail cancel payment.");
        }
    }

    public static class OrderServiceException extends ResponseStatusException {
        public OrderServiceException(HttpStatusCode statusCode, String reason) {
            super(statusCode, reason);
        }
    }
}
