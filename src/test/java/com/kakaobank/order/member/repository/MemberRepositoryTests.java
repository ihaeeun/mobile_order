package com.kakaobank.order.member.repository;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.Member;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.kakaobank.order.TestUtils.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTests {

	@Autowired
	MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void setUp() {
		member = TestUtils.buildMember();
	}

	@Test
	void findById() {
		// given
		var saved = this.memberRepository.save(member);

		// when
		var result = this.memberRepository.findById(saved.getId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.get().getUserId()).isEqualTo(saved.getUserId());
		assertThat(result.get().getPassword()).isEqualTo(saved.getPassword());
		assertThat(result.get().getPhone()).isEqualTo(saved.getPhone());
	}

	@Test
	void findByUserId() {
		// given
		var saved = this.memberRepository.save(member);

		// when
		var result = this.memberRepository.findByUserId(saved.getUserId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(saved.getUserId());
		assertThat(result.getPassword()).isEqualTo(saved.getPassword());
		assertThat(result.getPhone()).isEqualTo(saved.getPhone());
	}

	@Test
	void findByUserNameAndPhone() {
		// given
		var saved = this.memberRepository.save(member);

		// when
		var result = this.memberRepository.findByUserNameAndPhone(saved.getUserName(), saved.getPhone());

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(saved.getUserId());
		assertThat(result.getPassword()).isEqualTo(saved.getPassword());
		assertThat(result.getPhone()).isEqualTo(saved.getPhone());
	}

	@Test
	void save() {
		// when
		var result = this.memberRepository.save(member);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(USER_ID);
	}

}