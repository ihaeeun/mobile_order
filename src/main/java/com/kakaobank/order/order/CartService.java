package com.kakaobank.order.order;

import java.util.List;

import com.kakaobank.order.common.aop.UserAction;
import com.kakaobank.order.common.entity.ActionType;
import com.kakaobank.order.common.entity.CartItem;
import com.kakaobank.order.common.exception.MessageBuilder;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.order.dto.CartEntries;
import com.kakaobank.order.order.dto.CartItemInfo;
import com.kakaobank.order.order.dto.CartResponse;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.repository.CartItemRepository;
import com.kakaobank.order.product.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class CartService {
	private final ProductService productService;

	private final CartItemRepository cartItemRepository;

	public CartService(ProductService productService, CartItemRepository cartItemRepository) {
		this.productService = productService;
		this.cartItemRepository = cartItemRepository;
	}


	public CartEntries getCartList(String memberId) {
		var cartItemInfos = this.cartItemRepository.findCartItemResponseByMemberId(memberId).stream()
				.map((itemInfo) -> CartResponse.of(itemInfo, itemInfo.getStock() > itemInfo.getQuantity()))
				.toList();
		return new CartEntries(cartItemInfos);
	}

	public List<CartItemInfo> getCartItemInfos(List<Long> cartItemIds) {
		return this.cartItemRepository.findAllCartItemInfoById(cartItemIds);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@UserAction(actionType = ActionType.ADD_CART)
	public CartEntries addCart(UserContext context, AddCartRequest request) {
		var product = this.productService.getProduct(request.productId());

		if (product.isAvailable(request.quantity())) {
			var cartItem = this.cartItemRepository.findByMemberIdAndProductId(context.getUuid(), request.productId());

			// 카트에 이미 있는 상품인 경우 수량 추가, 카트에 없는 경우 카트에 상품 추가
			if (ObjectUtils.isEmpty(cartItem)) {
				var item = new CartItem(context.getUuid(), request.productId(), request.quantity());
				this.cartItemRepository.save(item);
			}
			else {
				cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
				this.cartItemRepository.save(cartItem);
			}

			return getCartList(context.getUuid());
		}
		else {
			var message = MessageBuilder.buildOutOfStockMessage(product.getName(), product.getStock());
			throw new OrderService.OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, message);
		}
	}

	@Transactional
	@UserAction(actionType = ActionType.DELETE_CART)
	public CartEntries deleteCartItems(UserContext context, DeleteCartItemRequest request) {
		this.cartItemRepository.deleteAllById(request.cartItemIds());
		return getCartList(context.getUuid());
	}


}
