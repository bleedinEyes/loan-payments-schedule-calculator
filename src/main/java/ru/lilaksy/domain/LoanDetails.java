package ru.lilaksy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanDetails {
    private BigDecimal loanAmount;
    private int loanTermMonths;
    private BigDecimal annualInterestRate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate loanStartDate;

    private PaymentType paymentType;
}
