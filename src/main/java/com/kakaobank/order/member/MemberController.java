package com.kakaobank.order.member;

import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.member.dto.SigninRequest;
import com.kakaobank.order.member.dto.SigninResponse;
import com.kakaobank.order.member.dto.SignupRequest;
import com.kakaobank.order.member.dto.SignupResponse;
import com.kakaobank.order.member.dto.WithdrawalResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberController {

	private final MemberService memberService;

	MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/signin")
	public SigninResponse signin(@RequestBody SigninRequest request) {
		return this.memberService.signIn(request);
	}

	@PostMapping("/signup")
	public SignupResponse signup(@RequestBody SignupRequest request) {
		return this.memberService.signUp(request);
	}

	@DeleteMapping("/withdrawal")
	public WithdrawalResponse withdraw(UserContext context) {
		return this.memberService.withdraw(context.getUuid());
	}

	@PutMapping("/withdrawal/cancellation")
	public WithdrawalResponse cancelWithdraw(@RequestBody SigninRequest request) {
		return this.memberService.cancelWithdraw(request);
	}

}
