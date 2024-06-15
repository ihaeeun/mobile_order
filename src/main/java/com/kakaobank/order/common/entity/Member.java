package com.kakaobank.order.common.entity;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.kakaobank.order.member.dto.SignupRequest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(indexes = @Index(name = "ix_userName_phone", columnList = "user_name, phone"))
public class Member {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;

	@Column(unique = true)
	private String userId;

	@Nonnull
	private String password;

	@Nonnull
	private String userName;

	@Nonnull
	@Column(unique = true)
	private String phone;

	@Nonnull
	private LocalDate birth;

	@Nonnull
	private Gender gender;

	private boolean withdrawal = false;

	@Nonnull
	private ZonedDateTime signupDatetime = ZonedDateTime.now();

	@Nullable
	private ZonedDateTime withdrawalDatetime = null;

	public Member() {
	}

	private Member(String userId, String password, String userName, String phone, LocalDate birth, Gender gender) {
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.phone = phone;
		this.birth = birth;
		this.gender = gender;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public boolean isWithdrawal() {
		return this.withdrawal;
	}

	public void setWithdrawal(boolean withdrawal) {
		this.withdrawal = withdrawal;
	}

	public void setSignupDatetime(ZonedDateTime signUpDateTime) {
		this.signupDatetime = signUpDateTime;
	}

	public ZonedDateTime getWithdrawalDatetime() {
		return this.withdrawalDatetime;
	}

	public void setWithdrawalDatetime(ZonedDateTime withdrawalDateTime) {
		this.withdrawalDatetime = withdrawalDateTime;
	}

	public static Member of(SignupRequest request, PasswordEncoder passwordEncoder) {
		var user = new Member(request.userId(), request.password(), request.name(), request.phone(), request.birth(),
				request.gender());
		return user.hashPassword(passwordEncoder);
	}

	private Member hashPassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
		return this;
	}

	public Member withdraw() {
		this.setWithdrawal(true);
		this.setWithdrawalDatetime(ZonedDateTime.now());
		return this;
	}

	public Member cancelWithdraw() {
		this.setWithdrawal(false);
		this.setWithdrawalDatetime(null);
		return this;
	}

	public boolean isAbleToCancelWithdrawal() {
		return this.withdrawalDatetime != null && this.withdrawalDatetime.isAfter(ZonedDateTime.now().minusDays(30));
	}

}
