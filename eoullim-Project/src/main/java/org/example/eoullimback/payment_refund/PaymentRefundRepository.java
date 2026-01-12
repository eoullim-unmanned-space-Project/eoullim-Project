package org.example.eoullimback.payment_refund;

import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRefundRepository extends JpaRepository<PaymentRefund, Long> {

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.user u
            WHERE u.id = :userId
            AND p.paymentKey = :paymentKey
            """)
    Optional<Payment> findByUserIdAndPaymentKey(@Param("userId")Long userId, @Param("paymentKey")String paymentKey);


    Optional<PaymentRefund> findByPayment(Payment paymentEntity);

    @Query("""
            SELECT pr FROM PaymentRefund pr
            JOIN FETCH pr.payment p
            JOIN FETCH p.user u
            JOIN FETCH p.booking b
            JOIN FETCH b.room r
            ORDER BY pr.createdAt DESC
            """)
    List<PaymentRefund> findAllWithPayment();

    @Query("""
           SELECT pr FROM PaymentRefund pr
           JOIN FETCH pr.payment p
           JOIN FETCH p.user u
           JOIN FETCH p.booking b
           JOIN FETCH b.room r
           WHERE pr.id = :id
           """)
    Optional<PaymentRefund> findByIdPayment(@Param("id")Long id);

    boolean existsByPayment(Payment payment);
}
