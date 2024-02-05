package com.example.be_study.service.user.enums;

import com.example.be_study.common.response.DataResponseCode;
import lombok.Getter;

@Getter
public enum UserResponseMessage implements DataResponseCode {

    SUCCESS(200,"정상 처리 완료"),
    TOKEN_EXPIRED_MESSAGE(200, "Token Expired Error"),
    NOT_FOUND_USER(200, "유저를 찾을 수 없습니다.");

    private final int responseStatus;

    private final String responseMessage;

    UserResponseMessage(int responseStatus, String responseMessage) {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
    }

}
