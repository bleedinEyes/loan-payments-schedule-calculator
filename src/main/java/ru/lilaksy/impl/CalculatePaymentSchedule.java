package ru.lilaksy.impl;

import ru.lilaksy.domain.PaymentSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CalculatePaymentSchedule {
    public static List<PaymentSchedule> calculate(double loanAmount, int loanTermMonths,
                                                  double annualInterestRate, LocalDate loanStartDate,
                                                  String paymentType) {
        List<PaymentSchedule> schedule = new ArrayList<>();
        double monthlyInterestRate = annualInterestRate / 12 / 100;

        if (paymentType.equalsIgnoreCase("a")) {

            double paymentAmount = loanAmount * monthlyInterestRate * (Math.pow(1 + monthlyInterestRate, loanTermMonths)) /
                    (Math.pow(1 + monthlyInterestRate, loanTermMonths) - 1);

            for (int month = 0; month < loanTermMonths; month++) {
                LocalDate paymentDate = loanStartDate.plusMonths(month);
                paymentDate = adjustPaymentDate(paymentDate);

                double interestPayment = loanAmount * monthlyInterestRate;
                double principalPayment = paymentAmount - interestPayment;
                loanAmount -= principalPayment;

                schedule.add(new PaymentSchedule(paymentDate, paymentAmount, principalPayment, interestPayment));
            }
        } else if (paymentType.equalsIgnoreCase("d")) {

            double principalPayment = loanAmount / loanTermMonths;

            for (int month = 0; month < loanTermMonths; month++) {
                LocalDate paymentDate = loanStartDate.plusMonths(month);
                paymentDate = adjustPaymentDate(paymentDate);

                double interestPayment = loanAmount * monthlyInterestRate;
                double paymentAmount = principalPayment + interestPayment;
                loanAmount -= principalPayment;

                schedule.add(new PaymentSchedule(paymentDate, paymentAmount, principalPayment, interestPayment));
            }
        }
        return schedule;
    }

    //======================================================================================================================================================
    //Implementation
    //======================================================================================================================================================

    private static LocalDate adjustPaymentDate(LocalDate paymentDate) {

        while (paymentDate.getDayOfWeek() == DayOfWeek.SATURDAY || paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY || isHoliday(paymentDate)) {
            paymentDate = paymentDate.plusDays(1);
        }

        return paymentDate;
    }

    private static boolean isHoliday(LocalDate paymentDate) {
        Set<LocalDate> holidays = Set.of(
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 1),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 2),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 3),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 4),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 5),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 6),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 7),
                LocalDate.of(paymentDate.getYear(), Month.JANUARY, 8),
                LocalDate.of(paymentDate.getYear(), Month.FEBRUARY, 23),
                LocalDate.of(paymentDate.getYear(), Month.MARCH, 8),
                LocalDate.of(paymentDate.getYear(), Month.MAY, 1),
                LocalDate.of(paymentDate.getYear(), Month.MAY, 9),
                LocalDate.of(paymentDate.getYear(), Month.JUNE, 12),
                LocalDate.of(paymentDate.getYear(), Month.NOVEMBER, 4)
        );

        return holidays.contains(paymentDate);
    }
}
