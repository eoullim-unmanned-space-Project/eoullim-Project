package org.example.eoullimback.admin.dasboard;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.booking.BookingResponse;
import org.example.eoullimback.booking.BookingService;
import org.example.eoullimback.payment.PaymentResponse;
import org.example.eoullimback.payment.PaymentService;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiDashBoardController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final PaymentRefundService paymentRefundService;
    private final UserService userService;

    @GetMapping("/api/admin/payments")
    ResponseEntity<List<PaymentResponse.PaymentListDTO>> getAllPaymentList() {

        List<PaymentResponse.PaymentListDTO> payments = paymentService.getAllPaymentList();

        return ResponseEntity.ok().body(payments);
    }

    @GetMapping("/api/admin/sales/today")
    ResponseEntity<PaymentResponse.SalesResponseDto> getTodaySales() {
        PaymentResponse.SalesResponseDto sales = paymentService.getTodaySales();
        return ResponseEntity.ok().body(sales);
    }

    @GetMapping("/api/admin/bookings/new/count")
    ResponseEntity<BookingResponse.CountDTO> getNewBookings() {
        BookingResponse.CountDTO todayBookingCount = bookingService.countTodayBookings();
        return ResponseEntity.ok().body(todayBookingCount);
    }

    @GetMapping("/api/admin/refunds/pending/count")
    ResponseEntity<Map<String, Long>> getRefundsPending() {
        return ResponseEntity.ok().body(Map.of("refundCounts", paymentRefundService.countPaymentsInRefundRequested()));
    }

    @GetMapping("/api/admin/category")
    public ResponseEntity<List<PaymentRefundResponse.RefundCategoryCountDTO>> getCategory() {

        List<PaymentRefundResponse.RefundCategoryCountDTO> categoryCountList = paymentRefundService.getRefundCategoryCounts();
        return ResponseEntity.ok().body(categoryCountList);
    }

    @GetMapping("/api/admin/users")
    public ResponseEntity<Map<String, Long>> getTodayUserCounts() {

        long todayCount = userService.countTodayUsers();
        long yesterdayCount = userService.countYesterdayUser();

        Map<String, Long> result = Map.of(
                "todayCount", todayCount,
                "yesterdayCount", yesterdayCount
        );

        return ResponseEntity.ok(result);
    }
}
