package ru.lilaksy.impl;

import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.domain.PaymentType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class PaymentScheduleCalculator {

    private final static Map<Month, Set<Integer>> HOLIDAYS = new HashMap<>() {{
        put(Month.JANUARY, Set.of(1, 2, 3, 4, 5, 6, 7, 8));
        put(Month.FEBRUARY, Set.of(23));
        put(Month.MARCH, Set.of(8));
        put(Month.MAY, Set.of(1, 9));
        put(Month.JUNE, Set.of(12));
        put(Month.NOVEMBER, Set.of(4));

    }};
    public List<PaymentSchedule> calculate(LoanDetails loanDetails) {
        List<PaymentSchedule> schedule = new ArrayList<>();

        BigDecimal loanAmount = loanDetails.getLoanAmount();
        int loanTermMonths = loanDetails.getLoanTermMonths();
        BigDecimal annualInterestRate = loanDetails.getAnnualInterestRate();
        LocalDate loanStartDate = loanDetails.getLoanStartDate();
        PaymentType paymentType = loanDetails.getPaymentType();

        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal totalPrincipal = loanAmount;

        if (paymentType == PaymentType.ANNUITY) {

            BigDecimal paymentAmount = loanAmount.multiply(monthlyInterestRate)
                    .multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(loanTermMonths))
                    .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(loanTermMonths).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

            for (int month = 0; month < loanTermMonths; month++) {
                LocalDate paymentDate = adjustPaymentDate(loanStartDate.plusMonths(month));

                BigDecimal interestPayment = loanAmount.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal principalPayment = paymentAmount.subtract(interestPayment);
                loanAmount = loanAmount.subtract(principalPayment);

                schedule.add(new PaymentSchedule(paymentDate, paymentAmount, principalPayment, interestPayment));
            }

            lastPaymentCorrection(schedule, totalPrincipal);

        } else if (paymentType == PaymentType.DIFFERENTIATED) {

            BigDecimal principalPayment = loanAmount.divide(BigDecimal.valueOf(loanTermMonths), 2, RoundingMode.HALF_UP);

            for (int month = 0; month < loanTermMonths; month++) {
                LocalDate paymentDate = adjustPaymentDate(loanStartDate.plusMonths(month));

                BigDecimal interestPayment = loanAmount.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal paymentAmount = principalPayment.add(interestPayment);
                loanAmount = loanAmount.subtract(principalPayment);

                schedule.add(new PaymentSchedule(paymentDate, paymentAmount, principalPayment, interestPayment));
            }

            lastPaymentCorrection(schedule, totalPrincipal);
        }
        return schedule;
    }

    //======================================================================================================================================================
    //Implementation
    //======================================================================================================================================================

    private void lastPaymentCorrection(List<PaymentSchedule> schedule, BigDecimal totalPrincipal) {
        BigDecimal totalPaid = schedule.stream()
                .map(PaymentSchedule::getPrincipalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(totalPrincipal) != 0) {
            BigDecimal difference = totalPrincipal.subtract(totalPaid);
            PaymentSchedule lastPayment = schedule.get(schedule.size() - 1);
            lastPayment.setPrincipalPayment(lastPayment.getPrincipalPayment().add(difference));
        }
    }

    private LocalDate adjustPaymentDate(LocalDate paymentDate) {

        while (paymentDate.getDayOfWeek() == DayOfWeek.SATURDAY || paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY || isHoliday(paymentDate)) {
            paymentDate = paymentDate.plusDays(1);
        }

        return paymentDate;
    }

    private boolean isHoliday(LocalDate paymentDate) {
        return HOLIDAYS.getOrDefault(paymentDate.getMonth(), Collections.emptySet())
                .contains(paymentDate.getDayOfMonth());
    }
}
