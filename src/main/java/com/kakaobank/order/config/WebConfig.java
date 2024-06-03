package com.kakaobank.order.config;

import java.util.List;

import com.kakaobank.order.common.util.JwtProvider;
import com.kakaobank.order.common.util.UserContextResolver;
import com.kakaobank.order.member.MemberService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtProvider jwtProvider;

	private final MemberService memberService;

	public WebConfig(JwtProvider jwtProvider, MemberService memberService) {
		this.jwtProvider = jwtProvider;
		this.memberService = memberService;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new UserContextResolver(this.jwtProvider, memberService));
	}

}
