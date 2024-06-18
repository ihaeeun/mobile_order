package com.kakaobank.order.product;

import com.kakaobank.order.product.dto.ProductEntries;
import com.kakaobank.order.product.dto.ProductResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ProductEntries getProducts() {
		return this.productService.getProducts();
	}

	@GetMapping("/{id}")
	public ProductResponse getProductDetail(@PathVariable long id) {
		return this.productService.getProductDetail(id);
	}

}
