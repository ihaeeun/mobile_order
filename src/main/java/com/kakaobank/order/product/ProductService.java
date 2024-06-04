package com.kakaobank.order.product;

import com.kakaobank.order.common.entity.Product;
import com.kakaobank.order.product.dto.ProductEntries;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ProductEntries getProducts() {
		var products = this.productRepository.findAll();
		return new ProductEntries(products);
	}

	public Product getProductDetail(long id) {
		return this.productRepository.findById(id)
				.orElseThrow(() -> new ProductServiceException(HttpStatus.NOT_FOUND, "There is no product"));
	}

	public void updateStock(long productId, int quantity) {
		this.productRepository.updateStock(productId, quantity);
	}

	private static final class ProductServiceException extends ResponseStatusException {

		private ProductServiceException(HttpStatusCode status, String reason) {
			super(status, reason);
		}

	}

}
