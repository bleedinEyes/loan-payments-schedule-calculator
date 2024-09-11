package ru.lilaksy;

import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.impl.CalculatePaymentSchedule;
import ru.lilaksy.impl.CreateExcelFile;
import ru.lilaksy.impl.LoanDetailsLoader;

import java.time.LocalDate;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            LoanDetails loanDetails = LoanDetailsLoader.load();
            LocalDate loanStartDate = LocalDate.parse(loanDetails.getLoanStartDate());

            List<PaymentSchedule> paymentSchedule = CalculatePaymentSchedule.calculate(
                    loanDetails.getLoanAmount(),
                    loanDetails.getLoanTermMonths(),
                    loanDetails.getAnnualInterestRate(),
                    loanStartDate,
                    loanDetails.getPaymentType()
            );

            CreateExcelFile.create(paymentSchedule);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}