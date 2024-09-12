package ru.lilaksy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PaymentSchedule {
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private BigDecimal principalPayment;
    private BigDecimal interestPayment;
}
