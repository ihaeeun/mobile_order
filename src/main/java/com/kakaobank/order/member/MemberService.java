package com.kakaobank.order.member;

import com.kakaobank.order.common.aop.MemberHistory;
import com.kakaobank.order.common.entity.ActionType;
import com.kakaobank.order.common.entity.Member;
import com.kakaobank.order.common.util.JwtProvider;
import com.kakaobank.order.member.dto.SigninRequest;
import com.kakaobank.order.member.dto.SigninResponse;
import com.kakaobank.order.member.dto.SignupRequest;
import com.kakaobank.order.member.dto.SignupResponse;
import com.kakaobank.order.member.dto.WithdrawalResponse;
import com.kakaobank.order.member.repository.MemberRepository;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final JwtProvider jwtProvider;

	private final PasswordEncoder passwordEncoder;

	MemberService(MemberRepository memberRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.jwtProvider = jwtProvider;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@MemberHistory(actionType = ActionType.SIGN_UP)
	public SignupResponse signUp(SignupRequest request) {
		// 가입 여부 확인
		if (isJoinedUser(request)) {
			throw new MemberServiceException(HttpStatus.BAD_REQUEST, "You are already joined.");
		}
		// id 중복 확인
		if (isDuplicatedId(request.userId())) {
			throw new MemberServiceException(HttpStatus.BAD_REQUEST, "Already used id");
		}

		var member = this.memberRepository.save(Member.of(request, this.passwordEncoder));
		return new SignupResponse(member.getUserId(), member.getUserName());
	}

	private boolean isJoinedUser(SignupRequest request) {
		return !ObjectUtils.isEmpty(this.memberRepository.findByUserNameAndPhone(request.name(), request.phone()));

	}

	private boolean isDuplicatedId(String userId) {
		return !ObjectUtils.isEmpty(this.memberRepository.findByUserId(userId));
	}

	@Transactional
	@MemberHistory(actionType = ActionType.SIGN_IN)
	public SigninResponse signIn(SigninRequest request) {
		var user = this.memberRepository.findByUserId(request.userId());

		if (ObjectUtils.isEmpty(user) || isInvalidatedUser(request, user)) {
			throw new MemberServiceException(HttpStatus.FORBIDDEN, "Wrong id or password.");
		}
		else if (user.isWithdrawal()) {
			throw new MemberServiceException(HttpStatus.FORBIDDEN, "You already withdrew");
		}

		var token = this.jwtProvider.generateToken(user.getUuid(), user.getUserId());
		return new SigninResponse(token);
	}

	private boolean isInvalidatedUser(SigninRequest request, Member user) {
		return !this.passwordEncoder.matches(request.password(), user.getPassword());
	}

	@Transactional
	@MemberHistory(actionType = ActionType.WITHDRAW)
	public WithdrawalResponse withdraw(String uuid) {
		var user = this.memberRepository.findById(uuid)
				.orElseThrow(() -> new MemberServiceException(HttpStatus.FORBIDDEN, "Wrong user."));
		user.withdraw();
		return new WithdrawalResponse(user.getUserId(), user.getUserName(), user.isWithdrawal());
	}

	@Transactional
	@MemberHistory(actionType = ActionType.CANCEL_WITHDRAW)
	public WithdrawalResponse cancelWithdraw(SigninRequest request) {
		var user = this.memberRepository.findByUserId(request.userId());

		if (ObjectUtils.isEmpty(user) || isInvalidatedUser(request, user)) {
			throw new MemberServiceException(HttpStatus.FORBIDDEN, "Wrong id or password.");
		}

		if (!user.isAbleToCancelWithdrawal()) {
			throw new MemberServiceException(HttpStatus.FORBIDDEN, "You withdrawal cancellation period has passed.");
		}

		user.cancelWithdraw();
		return new WithdrawalResponse(user.getUserId(), user.getUserName(), user.isWithdrawal());
	}

	public static class MemberServiceException extends ResponseStatusException {

		public MemberServiceException(HttpStatusCode statusCode, String reason) {
			super(statusCode, reason);
		}

	}

}
