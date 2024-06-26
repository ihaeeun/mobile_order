package com.kakaobank.order.config;

import java.util.List;

import com.kakaobank.order.common.util.JwtProvider;
import com.kakaobank.order.common.util.UserContextResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtProvider jwtProvider;

	public WebConfig(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new UserContextResolver(this.jwtProvider));
	}

}
