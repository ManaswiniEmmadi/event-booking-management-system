package com.event.booking.management.DTO;

import com.event.booking.management.model.enums.PaymentMethod;
import com.event.booking.management.model.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Integer paymentId;
    private Integer bookingId;
    private Integer userId;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
}
