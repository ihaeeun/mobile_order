package com.kakaobank.order.common.aop;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobank.order.member.repository.MemberHistoryRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberHistoryAdvice {

	private final MemberHistoryRepository memberHistoryRepository;

	private final ObjectMapper objectMapper;

	public MemberHistoryAdvice(MemberHistoryRepository memberHistoryRepository, ObjectMapper objectMapper) {
		this.memberHistoryRepository = memberHistoryRepository;
		this.objectMapper = objectMapper;
		this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Pointcut("@annotation(com.kakaobank.order.common.aop.MemberHistory)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
		var signature = (MethodSignature) joinPoint.getSignature();
		var method = signature.getMethod();

		// build UserHistory
		var actionType = method.getAnnotation(MemberHistory.class).actionType();
		var parameters = method.getParameters();
		var args = joinPoint.getArgs();
		var memberHistoryBuilder = com.kakaobank.order.common.entity.MemberHistory.builder().actionType(actionType);

		Map<String, Object> requestParam = new HashMap<>();

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				var parameter = parameters[i];
				if (parameter != null) {
					var name = parameter.getName();
					requestParam.put(name, args[i]);
				}
			}
		}

		final Object result = joinPoint.proceed();
		if (result != null) {
			memberHistoryBuilder.request(this.objectMapper.writeValueAsString(requestParam))
				.result(this.objectMapper.writeValueAsString(result))
				.actionDateTime(ZonedDateTime.now());
		}

		var memberHistory = memberHistoryBuilder.build();
		this.memberHistoryRepository.save(memberHistory);

		return result;
	}

}
