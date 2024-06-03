package com.kakaobank.order.member;

import com.kakaobank.order.common.entity.Member;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.member.dto.SigninRequest;
import com.kakaobank.order.member.dto.SignupRequest;

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
	public String signin(@RequestBody SigninRequest request) {
		return this.memberService.signIn(request);
	}

	@PostMapping("/signup")
	public boolean signup(@RequestBody SignupRequest request) {
		return this.memberService.signUp(request);
	}

	@DeleteMapping("/withdrawal")
	public Member withdraw(UserContext context) {
		return this.memberService.withdraw(context.getUuid());
	}

	@PutMapping("/withdrawal/cancellation")
	public Member cancelWithdraw(@RequestBody SigninRequest request) {
		return this.memberService.cancelWithdraw(request);
	}

}
