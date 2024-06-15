package com.kakaobank.order.member;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.Gender;
import com.kakaobank.order.common.util.JwtProvider;
import com.kakaobank.order.member.dto.SigninRequest;
import com.kakaobank.order.member.dto.SignupRequest;
import com.kakaobank.order.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTests {

	@InjectMocks
	MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void signUp() {
		// given
		given(this.memberRepository.findByUserNameAndPhone(any(), any())).willReturn(null);
		given(this.memberRepository.findByUserId(TestUtils.USER_ID)).willReturn(null);
		given(this.memberRepository.save(any())).willReturn(TestUtils.buildMember());
		var request = new SignupRequest(TestUtils.USER_ID, "secret", "testUser", "01011112222",
				LocalDate.of(2020, 01, 01), Gender.MALE);

		// when
		var result = this.memberService.signUp(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.userId()).isEqualTo(request.userId());
		assertThat(result.userName()).isEqualTo(request.name());
	}

	@Test
	void signUp_joinedUser() {
		// given
		var request = new SignupRequest(TestUtils.USER_ID, "secret", "testUser", "01011112222",
				LocalDate.of(2020, 01, 01), Gender.MALE);
		var member = TestUtils.buildMember();
		given(this.memberRepository.findByUserNameAndPhone(any(), any())).willReturn(member);

		// when, then
		assertThatThrownBy(() -> this.memberService.signUp(request))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("already joined");
	}

	@Test
	void signUp_duplicatedId() {
		// given
		var request = new SignupRequest(TestUtils.USER_ID, "secret", "testUser", "01011112222",
				LocalDate.of(2020, 01, 01), Gender.MALE);
		var member = TestUtils.buildMember();
		given(this.memberRepository.findByUserNameAndPhone(any(), any())).willReturn(null);
		given(this.memberRepository.findByUserId(any())).willReturn(member);

		// when, then
		assertThatThrownBy(() -> this.memberService.signUp(request))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("used id");
	}

	@Test
	void signIn() {
		// given
		var user = TestUtils.buildMember();
		given(this.memberRepository.findByUserId(any())).willReturn(user);
		given(this.passwordEncoder.matches(any(), any())).willReturn(true);
		given(this.jwtProvider.generateToken(any(), any())).willReturn("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJoYWlsZXkiLCJ1dWlkIjoiMDlmYjU5MzktODI1Yy00NWQ0LTg3Y2QtYjhiYWMxMzNmNzZkIn0.fRkx44hXDpX7MRcuBYH05ejkqDBU6GoYOd14d63pJuo");
		var request = new SigninRequest(TestUtils.USER_ID, "secret");

		// when
		var result = this.memberService.signIn(request);

		// then
		assertThat(result).isNotNull();
		assertThat(StringUtils.hasText(result.token()));
	}

	@Test
	void signIn_unvalidated() {
		// given
		var user = TestUtils.buildMember();
		given(this.memberRepository.findByUserId(any())).willReturn(user);
		given(this.passwordEncoder.matches(any(), any())).willReturn(false);
		var request = new SigninRequest(TestUtils.USER_ID, "secret");

		// when, then
		assertThatThrownBy(() -> this.memberService.signIn(request))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("Wrong id");
	}

	@Test
	void signIn_withdrawnMember() {
		// given
		var user = TestUtils.buildMember();
		user.setWithdrawalDatetime(ZonedDateTime.now());
		user.setWithdrawal(true);
		given(this.memberRepository.findByUserId(any())).willReturn(user);
		given(this.passwordEncoder.matches(any(), any())).willReturn(true);
		var request = new SigninRequest(TestUtils.USER_ID, "secret");

		// when, then
		assertThatThrownBy(() -> this.memberService.signIn(request))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("already withdrew");
	}

	@Test
	void withdraw() {
		// given
		var member = TestUtils.buildMember();
		var user = Optional.of(member);
		given(this.memberRepository.findById(any())).willReturn(user);

		// when
		var result = this.memberService.withdraw(member.getUuid());

		// then
		assertThat(result).isNotNull();
		assertThat(result.withdrawal()).isTrue();
	}

	@Test
	void withdraw_fail() {
		// given
		given(this.memberRepository.findById(any())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> this.memberService.withdraw(any()))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("Wrong user");

	}

	@Test
	void cancelWithdraw() {
		// given
		var user = TestUtils.buildMember();
		user.setWithdrawal(true);
		user.setWithdrawalDatetime(ZonedDateTime.now().minusDays(7));
		given(this.memberRepository.findByUserId(any())).willReturn(user);
		given(this.passwordEncoder.matches(any(), any())).willReturn(true);
		var request = new SigninRequest(user.getUserId(), user.getPassword());

		// when
		var result = this.memberService.cancelWithdraw(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.withdrawal()).isFalse();
	}

	@Test
	void cancelWithdrawAfter30Days() {
		// given
		var user = TestUtils.buildMember();
		user.setWithdrawal(true);
		user.setWithdrawalDatetime(ZonedDateTime.now().minusDays(40));
		given(this.memberRepository.findByUserId(any())).willReturn(user);
		given(this.passwordEncoder.matches(any(), any())).willReturn(true);
		var request = new SigninRequest(user.getUserId(), user.getPassword());

		// when, then
		assertThatThrownBy(() -> this.memberService.cancelWithdraw(request))
				.isInstanceOf(MemberService.MemberServiceException.class)
				.hasMessageContaining("has passed");
	}

}
