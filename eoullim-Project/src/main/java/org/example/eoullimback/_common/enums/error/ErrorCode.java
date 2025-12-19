package org.example.eoullimback._common.enums.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ====
    // 400 Bad Request
    // ====
    INVALID_INPUT("잘못된 입력값입니다."),
    MISSING_PARAMETER("비워 둘수 없는 항목입니다."),

    // ====
    // 401 Unauthorized
    // ====
    UNAUTHORIZED("인증이 필요합니다."),


    // ====
    // 403 Forbidden
    // ====
    ACCESS_DENIED("접근 권한이 없습니다."),
    MY_ONLY("본인만 접근할 수 있습니다."),
    ADMIN_OLLY("관리자만 접근 할 수 있습니다."),

    // ====
    // 404 Not Found
    // ====
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    PLACE_NOT_FOUND("장소를 찾을 수 없습니다."),
    ROOM_NOT_FOUND("방을 찾을 수 없습니다."),
    TIMESLOT_NOT_FOUND("시간대를 찾을 수 없습니다."),
    ITEM_NOT_FOUND("정보을 찾을 수 없습니다."),

    // ====
    // 500 Internal Server Error
    // ====
    INTERNAL_ERROR("서버 내부 오류가 발했습니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
