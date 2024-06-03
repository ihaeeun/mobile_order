package com.kakaobank.order.common.util;

import com.kakaobank.order.member.MemberService.MemberServiceException;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserContextResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;

	public UserContextResolver(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserContext.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (parameter.getParameterType().equals(UserContext.class)) {
			var header = webRequest.getHeader("Authorization");

			if (StringUtils.hasText(header)) {
				var claims = this.jwtProvider.parseClaims(header);
				var uuid = claims.get("uuid").toString();
				var userId = claims.get("userId").toString();
				var reqPath = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
				return new UserContext(uuid, userId, reqPath);
			}
		}
		throw new MemberServiceException(HttpStatus.UNAUTHORIZED, "You need to login first.");
	}

}
