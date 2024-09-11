package ru.lilaksy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PaymentSchedule {
    private LocalDate paymentDate;
    private double paymentAmount;
    private double principalPayment;
    private double interestPayment;
}
