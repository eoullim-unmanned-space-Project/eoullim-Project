package org.example.eoullimback.geminichatbot;

import org.example.eoullimback._common.enums.geminichatbot.Intent;

public class CannedResponses {
    public static String respond(Intent intent) {
        return switch (intent) {
            case GREETING -> "안녕하세요! 무엇을 도와드릴까요?";
            case BOOKING -> """
                    카테고리 공간대여를 통해 예약 및 날짜와 시간을 안내하고 있습니다.
                    예약 후 5분 이내 결재 시도를 하지 않을 경우 자동 예약 취소되니 참고 바람니다.
                    자세한 사항은 QnA를 통해 문의해주세요.""";
            case REFUND -> """
                    환불은 예약 시간 전까지 사용하지 않은 경우에 한해 가능합니다.
                    자세한 사항은 QnA를 통해 문의해주세요.""";
            case QNA -> "QnA는 상단에 회원정보 카테고리에서 문의하실 수 있습니다. \n" +
                    "이용 문의 및 건의 사항도 접수 가능합니다.";
            case PLACE -> "저희는 파티룸, 스터디룸, 연습실 등의 공간 대여를 중개하고 있습니다.";
            case PRICE ->  """
                    요금은 장소별로 상이하며,
                    기본 요금은 50,000원 ~ 125,000원입니다.
                    최대 인원 초과 시 1인당 15,000원이 추가됩니다.
                    """;
            case LOCATION -> "카테고리 내 지도를 통해 위치 정보를 확인하실 수 있습니다.";
            case HOURS -> "무인 대여 공간으로 24시간 이용 가능합니다.";
            case UNKNOWN -> null;
        };
    }
}