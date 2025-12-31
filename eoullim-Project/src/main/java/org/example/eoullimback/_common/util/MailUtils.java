package org.example.eoullimback._common.util;

import java.util.Random;

public class MailUtils {

    public static String generateRandomCode() {
        Random random = new Random();

        int code = 100_000 + random.nextInt(900_000);

        return String.valueOf(code);
    }
}
