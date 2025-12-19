package org.example.eoullimback._common.util;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final ZoneId Z0NE_KST = ZoneId.of("Asia/Seoul");
    public static final ZoneId Z0NE_UTC = ZoneId.of("UTC");

    private static final DateTimeFormatter KST_FORMAT
            = DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss"));

    private static final DateTimeFormatter ISO_UTC_FORMAT
            = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String toKstString(LocalDateTime utcLocalDateTime) {
        if (utcLocalDateTime == null) return null;

        ZonedDateTime zdtUtc = utcLocalDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtKst = utcLocalDateTime.atZone(Z0NE_KST);

        return zdtKst.format(KST_FORMAT);
    }
}
