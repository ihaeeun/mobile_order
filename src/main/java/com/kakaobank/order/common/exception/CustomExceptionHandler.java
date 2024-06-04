package com.kakaobank.order.common.exception;

import com.kakaobank.order.member.MemberService.MemberServiceException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler
	public ProblemDetail handleRuntimeException(RuntimeException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

	@ExceptionHandler
	public ProblemDetail handleMemberException(MemberServiceException exception) {
		return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());
	}

	@ExceptionHandler
	public ProblemDetail handleSqlException(DataAccessException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
	}

}
