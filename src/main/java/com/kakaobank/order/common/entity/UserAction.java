package com.kakaobank.order.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAction {
    @Id
    @GeneratedValue
    private long id;

    private String userId;

    private String actionType;

    private String apiPath;

    @Column(columnDefinition = "TEXT")
    private String request;

    @Column(columnDefinition = "TEXT")
    private String result;

    private ZonedDateTime actionDateTime = ZonedDateTime.now();

}
