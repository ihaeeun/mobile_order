package com.kakaobank.order.common.entity;

import com.kakaobank.order.member.dto.SignupRequest;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "ix_userName_phone", columnList = "user_name, phone"))
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String userId;

    private String password;

    private String userName;

    @Column(unique = true)
    private String phone;

    private LocalDate birth;

    private Gender gender;

    private boolean withdrawal = false;

    private ZonedDateTime signUpDateTime = ZonedDateTime.now();

    @Nullable
    private ZonedDateTime withdrawalDateTime = null;

    public Member(String userId, String password, String userName, String phone, LocalDate birth, Gender gender) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.birth = birth;
        this.gender = gender;
    }

    public static Member of(SignupRequest request, PasswordEncoder passwordEncoder) {
        var user = new Member(request.userId(), request.password(), request.name(), request.phone(), request.birth(), request.gender()).hashPassword(passwordEncoder);
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
        return this.withdrawalDateTime.isAfter(ZonedDateTime.now().minusDays(30));
    }

    public enum Gender {
        MALE, FEMALE
    }
}
