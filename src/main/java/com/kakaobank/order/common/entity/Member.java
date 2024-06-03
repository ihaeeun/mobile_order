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
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

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
	private ZonedDateTime signUpDateTime = ZonedDateTime.now();

	@Nullable
	private ZonedDateTime withdrawalDateTime = null;

	public Member() {
	}

	public Member(String userId, String password, String userName, String phone, LocalDate birth, Gender gender) {
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.phone = phone;
		this.birth = birth;
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
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
		return withdrawal;
	}

	public void setWithdrawal(boolean withdrawal) {
		this.withdrawal = withdrawal;
	}

	public void setSignUpDateTime(ZonedDateTime signUpDateTime) {
		this.signUpDateTime = signUpDateTime;
	}

	public ZonedDateTime getWithdrawalDateTime() {
		return withdrawalDateTime;
	}

	public void setWithdrawalDateTime(ZonedDateTime withdrawalDateTime) {
		this.withdrawalDateTime = withdrawalDateTime;
	}

	public static Member of(SignupRequest request, PasswordEncoder passwordEncoder) {
		var user = new Member(request.userId(), request.password(), request.name(), request.phone(), request.birth(),
				request.gender())
			.hashPassword(passwordEncoder);
		return user.hashPassword(passwordEncoder);
	}

	private Member hashPassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
		return this;
	}

	public boolean checkPassword(String plain, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(plain, this.password);
	}

	public Member withdraw() {
		this.setWithdrawal(true);
		this.setWithdrawalDateTime(ZonedDateTime.now());
		return this;
	}

	public Member cancelWithdraw() {
		this.setWithdrawal(false);
		this.setWithdrawalDateTime(null);
		return this;
	}

	public boolean isAbleToCancelWithdrawal() {
		return this.withdrawalDateTime != null && this.withdrawalDateTime.isAfter(ZonedDateTime.now().minusDays(30));
	}

	public enum Gender {

		MALE, FEMALE

	}

}
