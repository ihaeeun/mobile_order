package com.kakaobank.order.common.aop;

import java.time.ZonedDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.member.repository.UserActionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActionAdvice {

	private final UserActionRepository userActionRepository;

	private final ObjectMapper objectMapper;

	public UserActionAdvice(UserActionRepository userActionRepository, ObjectMapper objectMapper) {
		this.userActionRepository = userActionRepository;
		this.objectMapper = objectMapper;
	}

	@Pointcut("@annotation(com.kakaobank.order.common.aop.UserAction)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
		var signature = (MethodSignature) joinPoint.getSignature();
		var method = signature.getMethod();

		// build UserAction
		var actionType = method.getAnnotation(UserAction.class).actionType();
		var parameters = method.getParameters();
		var args = joinPoint.getArgs();
		var userActionBuilder = com.kakaobank.order.common.entity.UserAction.builder().actionType(actionType);
		var requestParam = new HashMap<>();

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				var parameter = parameters[i];
				if (parameter != null) {
					var name = parameter.getName();
					if (name.equals("context")) {
						var context = (UserContext) args[i];
						userActionBuilder.memberId(context.getUuid()).apiPath(context.getReqPath());
					}
					else {
						requestParam.put(name, args[i]);
					}
				}
			}
		}

		final Object result = joinPoint.proceed();
		if (result != null) {
			userActionBuilder.request(this.objectMapper.writeValueAsString(requestParam))
				.result(this.objectMapper.writeValueAsString(result))
				.actionDateTime(ZonedDateTime.now());
		}

		var userAction = userActionBuilder.build();
		this.userActionRepository.save(userAction);

		return result;
	}

}
