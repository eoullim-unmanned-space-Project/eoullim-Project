package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback.user_auth.user.dto.response.UserCountResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardUserServiceImpl implements DashboardUserService {

    private final UserRepository userRepository;

    public UserCountResult getUserCount() {

        long todayCount = userRepository.countByStatus(Status.ACTIVE);

        LocalDateTime yesterdayEnd =
                LocalDate.now()
                        .minusDays(1)
                        .atTime(23, 59, 59, 999_999_999);

        long yesterdayCount = userRepository.countByStatusAndCreatedAtLessThanEqual(
                Status.ACTIVE,
                yesterdayEnd
        );

        return new UserCountResult(todayCount, yesterdayCount);
    }
}

