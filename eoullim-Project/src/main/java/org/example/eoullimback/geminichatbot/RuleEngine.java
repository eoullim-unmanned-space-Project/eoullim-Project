package org.example.eoullimback.geminichatbot;

public class RuleEngine {

    public static Intent match(String text) {
        String t = normalize(text);

        if (t.matches(".*(안녕|ㅎㅇ|하이|hello|hey).*")) return Intent.GREETING;
        if (t.matches(".*(예약|예약가능|시간예약|날짜예약).*")) return Intent.BOOKING;
        if (t.matches(".*(환불|취소|결제취소).*")) return Intent.REFUND;
        if (t.matches(".*(문의|QnA|건의|질의응답|문의위치|문의방법)")) return Intent.QNA;
        if (t.matches(".*(어떤공간|무슨공간|대여공간|종류|공간안내).*")) return Intent.PLACE;
        if (t.matches(".*(가격|요금|얼마|비용).*")) return Intent.PRICE;
        if (t.matches(".*(주소|위치|찾아가|가는길).*")) return Intent.LOCATION;
        if (t.matches(".*(운영시간|영업시간|몇시|언제열어).*")) return Intent.HOURS;

        return Intent.UNKNOWN;
    }

    private static String normalize(String text) {
        return text == null ? "" : text.replaceAll("\\s+", "").toLowerCase();
    }
}
