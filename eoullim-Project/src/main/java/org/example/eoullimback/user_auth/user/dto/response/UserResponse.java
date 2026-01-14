package org.example.eoullimback.user_auth.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback._common.enums.user.OAuthProvider;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment_refund.PaymentRefund;
import org.example.eoullimback.timeslot.TimeSlotResponse;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public record UserResponse() {

    @Data
    public static class AdminUserDetailDTO {
        private Long id;
        private String loginId;
        private String name;
        private String phone;
        private String email;
        private Status status;
        private OAuthProvider provider;
        private String displayProvider;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String suspendedReason;
        private LocalDateTime suspendedAt;
        private LocalDateTime withdrawnAt;

        public AdminUserDetailDTO(User user) {
            this.id = user.getId();
            this.loginId = user.getLoginId();
            this.name = user.getName();
            this.phone = user.getPhone() != null ? user.getPhone() : "/";
            this.email = user.getEmail() != null ? user.getEmail() : "/";
            this.status = user.getStatus();
            this.provider = user.getProvider() != null
                    ? user.getProvider()
                    : OAuthProvider.LOCAL;

            this.createdAt = user.getCreatedAt();
            this.updatedAt = user.getUpdatedAt();

            this.displayProvider = switch (this.provider) {
                case KAKAO -> "KAKAO";
                case NAVER -> "NAVER";
                case LOCAL -> "LOCAL";
                case GOOGLE -> "GOOGLE";
            };

            this.suspendedReason = user.getSuspendedReason();
            this.suspendedAt = user.getSuspendedAt();
            this.withdrawnAt = user.getWithdrawnAt();
        }
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AdminListDTO {
        private Long id;
        private String loginId;
        private String name;
        private Status status;
        private String displayStatus;
        private String statusClass;
        private OAuthProvider provider;
        private String displayProvider;
        private String providerClass;
        private LocalDate createdAt;

        public AdminListDTO(User user) {
            this.id = user.getId();
            this.loginId = user.getLoginId();
            this.name = user.getName();
            this.status = user.getStatus();
            this.provider = user.getProvider() != null
                    ? user.getProvider()
                    : OAuthProvider.LOCAL;
            this.createdAt = LocalDate.from(user.getCreatedAt());

            this.displayStatus = switch (this.status) {
                case ACTIVE -> "활성화";
                case WITHDRAWN -> "탈퇴";
                case SUSPENDED -> "정지";
            };

            this.statusClass = switch (this.status) {
                case ACTIVE -> "active";
                case WITHDRAWN -> "withdrawn";
                case SUSPENDED -> "suspended";
                default -> "unknown";
            };


            this.displayProvider = switch (this.provider) {
                case KAKAO -> "KAKAO";
                case NAVER -> "NAVER";
                case LOCAL -> "LOCAL";
                case GOOGLE -> "GOOGLE";
            };

            this.providerClass = switch (this.provider) {
                case KAKAO -> "provider-badge provider-kakao";
                case LOCAL -> "provider-badge provider-local";
                default -> "provider-badge provider-local";
            };
        }
    }

    public record Detail(
            String name,
            String email,
            String phone,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Detail from(User user) {
            return new Detail(
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        }
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OAuthToken {
        private String tokenType;
        private String accessToken;
        private String expiresIn;
        private String refreshToken;
        private String refreshTokenExpiresIn;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoProfile {
        private Long id;
        private String connectedAt;
        private Properties properties;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Data
    @NoArgsConstructor
    public static class UserBookingDTO {
        private String bookingCode;
        private String status;
        private String roomName;
        private Long amount;
        private List<TimeSlotResponse.DetailDTO> timeSlots;

        private String displayTime;

        public static UserBookingDTO empty() {
            UserBookingDTO dto = new UserBookingDTO();
            dto.roomName = "예약 내역이 없습니다";
            dto.amount = 0L;
            dto.bookingCode = "-";
            dto.status = "NONE";
            dto.timeSlots = Collections.emptyList();
            dto.displayTime = "";
            return dto;
        }

        public UserBookingDTO(List<Booking> bookings) {
            Booking grouping = bookings.get(0);
            this.roomName = grouping.getRoom().getName();
            this.amount = grouping.getItemSnapshotPrice();
            this.bookingCode = grouping.getBookingCode();
            this.status = grouping.getStatus().getFormatter();
            // 3개의 타임슬롯을 List로 묶어준다
            this.timeSlots = bookings.stream()
                    .map(booking -> new TimeSlotResponse.DetailDTO(booking.getTimeSlot())).toList();

            if (!timeSlots.isEmpty()) {
                TimeSlotResponse.DetailDTO first = timeSlots.get(0);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM월.dd일 HH:mm");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                this.displayTime = first.getStartTime().format(dateTimeFormatter) + "시"
                        + " ~ "
                        + first.getEndTime().format(timeFormatter) + "시";
            }
        }
    }

    @Data
    public static class UserPaymentDTO {
        private String name;
        private String roomName;
        private int qty;
        private Long amount;
        private List<TimeSlotResponse.DetailDTO> timeSlots;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime approvedAt;
        private String displayTime;
        private String bookingCode;
        private String displayStatus;
        private String paymentKey;
        private RefundStatus refundStatus;

         public UserPaymentDTO(Payment payment, List<TimeSlotResponse.DetailDTO> timeSlots, PaymentRefund paymentRefund) {
            this.name = payment.getUser().getName();
            this.roomName = payment.getBooking().getRoom().getName();
            this.amount = payment.getAmount();
            this.qty = payment.getBooking().getQty();
            this.paymentMethod = payment.getMethod();
            this.paymentStatus = payment.getStatus();
            this.approvedAt = payment.getApprovedAt();
            this.timeSlots = timeSlots;
            this.bookingCode = payment.getOrderId();
            this.paymentKey = payment.getPaymentKey();
            this.refundStatus = (paymentRefund != null) ? paymentRefund.getStatus() : null;

           this.displayStatus =  switch (this.paymentStatus) {
                case READY -> "결제준비";
                case REFUNDED -> "환불완료";
                case CANCELLED -> "결제취소";
                case FAILED -> "결제실패";
                case SUCCESS -> "결제성공";
               case COMPLETED -> "이용완료";
            };

            if (!timeSlots.isEmpty()) {
                TimeSlotResponse.DetailDTO first = timeSlots.get(0);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm -");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(" HH:mm까지");

                this.displayTime = first.getStartTime().format(dateTimeFormatter)
                        + first.getEndTime().format(timeFormatter);
            }
        }
    }
}
