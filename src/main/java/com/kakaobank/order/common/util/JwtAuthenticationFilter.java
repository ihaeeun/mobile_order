package com.kakaobank.order.common.util;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var token = request.getHeader("Authorization");

		if (token != null) {
			filterChain.doFilter(request, response);
		}
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Signin first.");
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String[] excludePath = { "/users", "/h2-console", "/products" };
		String path = request.getRequestURI();
		return Arrays.stream(excludePath).anyMatch(path::startsWith);
	}

}
