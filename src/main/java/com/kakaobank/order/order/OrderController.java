package com.kakaobank.order.order;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.order.dto.CartEntries;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.dto.OrderEntries;
import com.kakaobank.order.order.dto.OrderRequest;

import org.springframework.aop.MethodMatcher;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	private final OrderService orderService;

	OrderController(OrderService orderService) {
		this.orderService = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(), new Class[]{OrderService.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equals("addCart")) {
					System.out.println("proxy : before addCart");
					var invoke = method.invoke(orderService, args);
					System.out.println("proxy : after addCart");
					return invoke;
				}

				return method.invoke(orderService, args);
			}
		});
	}

	MethodInterceptor handler = new MethodInterceptor() {
		private final MethodMatcher methodMatcher;

		OrderService orderService;

		public MethodInterceptor(MethodMatcher m) {
			this.methodMatcher = m;
		}


		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (methodMatcher.matches(method, OrderService.class)) {
				return method.getName();
			}
			return null;
		}
	}

	@PostMapping("/cart")
	public CartEntries addCart(UserContext context, @RequestBody AddCartRequest request) {
		this.orderService.addCart(context, request);
		return this.orderService.getCartList(context.getUuid());
	}

	@GetMapping("/cart")
	public CartEntries getCart(UserContext context) {
		return this.orderService.getCartList(context.getUuid());
	}

	@DeleteMapping("/cart")
	public CartEntries deleteCartItems(UserContext context, @RequestBody DeleteCartItemRequest request) {
		return this.orderService.deleteCartItems(context, request);
	}

	@PostMapping("/order")
	public Order order(UserContext context, @RequestBody OrderRequest request) {
		return this.orderService.makeOrder(context, request);
	}

	@GetMapping("/order/history")
	public OrderEntries orderHistory(UserContext context) {
		return this.orderService.getOrderHistory(context.getUuid());
	}

	@DeleteMapping("/order/{orderId}")
	public Order cancelOrder(UserContext context, @PathVariable String orderId) {
		return this.orderService.cancelOrder(context, orderId);
	}

}
