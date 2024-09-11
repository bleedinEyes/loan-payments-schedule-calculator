package ru.lilaksy.domain;

import lombok.Data;

@Data
public class LoanDetails {
    private double loanAmount;
    private int loanTermMonths;
    private double annualInterestRate;
    private String loanStartDate;
    private String paymentType;
}
