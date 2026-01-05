package org.example.eoullimback._common.enums.errors;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ====
    // 400 Bad Request
    // ====
    INVALID_INPUT("잘못된 입력값입니다."),
    MISSING_PARAMETER("비워 둘수 없는 항목입니다."),
    INVALID_EMAIL_FORMAT("올바른 이메일 형식이 아닙니다."),
    MISSING_EMAIL("이메일을 입력해주세요."),
    ONLY_FILE_IMG("이미지 파일만 업로드 가능합니다."),
    MAX_FILE_IMG("파일 이미지는 5장 이어야 합니다."),
    ALREADY_BOOKED_TIME_SLOT("이미 예약된 타임슬롯입니다."),
    INVALID_BOOKING_STATUS("결제 가능한 상태가 아닙니다."),
    PAYMENT_ALREADY_EXISTS("이미 생성된 결제입니다."),
    PAYMENT_COMPLETED("이미 처리된 결제입니다."),
    PAYMENT_FAILED("포트원 인증실패: 관리자에게 문의하세요"),
    PAYMENT_AMOUNT_MISMATCH("금액이 다릅니다."),
    PAYMENT_ALREADY_PROCESSED("이미 결제된 주문입니다."),

    // ====
    // 401 Unauthorized
    // ====
    LOGIN_UNAUTHORIZED("로그인 인증이 필요합니다."),

    // ====
    // 403 Forbidden
    // ====
    ACCESS_DENIED("접근 권한이 없습니다."),
    MY_ONLY("본인만 접근할 수 있습니다."),
    ADMIN_OLLY("관리자만 접근 할 수 있습니다."),
    LOGIN_ONLY("로그인이 필요합니다."),

    // ====
    // 404 Not Found
    // ====
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    PLACE_NOT_FOUND("장소를 찾을 수 없습니다."),
    ROOM_NOT_FOUND("방을 찾을 수 없습니다."),
    ROOM_IMG_NOT_FOUND("이미지를 찾을 수 없습니다."),
    TIMESLOT_NOT_FOUND("시간대를 찾을 수 없습니다."),
    ITEM_NOT_FOUND("정보을 찾을 수 없습니다."),
    QAA_NOT_FOUND("Q&A을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),
    NOTICE_NOT_FOUND("존재하지 않는 공지사항 입니다."),
    REVIEW_NOT_FOUND("리뷰가 존재하지 않습니다."),
    ROOM_STATE_NOT_FOUND("OPEN상태인 방이 존재하지 않습니다."),
    BOOKING_NOT_FOUND("부킹 내역이 존재하지 않습니다."),
    BOOKING_CODE_NOT_FOUND("부킹 코드의 내역이 존재하지 않습니다."),
    PAYMENT_NOT_FOUND("결제 내역이 존재하지 않습니다"),
    PAYMENT_DATA_NOT_FOUND("결제 데이터가 존재하지 않습니다."),


    // ====
    // 409 Conflict
    // ====
    USER_CONFLICT_ID("이미 존재하는 아이디입니다."),
    USER_CONFLICT_PASSWORD("이미 존재하는 비밀번호입니다."),
    USER_CONFLICT_EMAIL("이미 존재하는 이메일입니다."),
    USER_CONFLICT_PHONE_NUMBER("이미 존재하는 휴대폰 번호 입니다."),
    USER_STATUS_WITHDRAWN("이미 탈퇴한 회원입니다. 고객센터에 문의해주세요."),
    REVIEW_CONFLICT("이미 작성된 리뷰입니다."),

    // ====
    // 500 Internal Server Error
    // ====
    INTERNAL_ERROR("서버 내부 오류가 발했습니다."),
    FILE_SAVE_FAILED("파일 저장에 실패했습니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
