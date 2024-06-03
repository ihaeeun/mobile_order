package com.kakaobank.order.product;

import java.util.List;

import com.kakaobank.order.common.entity.Product;

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

	public List<Product> getProducts() {
		return this.productRepository.findAll();
	}

	public List<Product> getProducts(List<Long> ids) {
		return this.productRepository.findAllById(ids);
	}

	public Product getProductDetail(long id) {
		return this.productRepository.findById(id)
			.orElseThrow(() -> new ProductServiceException(HttpStatus.NOT_FOUND, "Thee is no product"));
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
