package org.example.eoullimback.user_auth.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCountResult {

    private final long todayCount;
    private final long yesterdayCount;
}
