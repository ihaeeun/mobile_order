package com.kakaobank.order.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private String uuid;
    private String userId;
    private String reqPath;
}
