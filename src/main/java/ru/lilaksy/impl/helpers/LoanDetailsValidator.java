package ru.lilaksy.impl.helpers;

import ru.lilaksy.domain.LoanDetails;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LoanDetailsValidator {

    HolidayChecker holidayChecker = new HolidayChecker();

    public void validateLoanDetails(LoanDetails loanDetails) throws IllegalArgumentException {
        if (loanDetails.getLoanAmount() == null || loanDetails.getLoanAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("loanAmount должен быть больше 0.");
        }

        if (loanDetails.getLoanTermMonths() <= 6 || loanDetails.getLoanTermMonths() >= 120) {
            throw new IllegalArgumentException("loanTermMonths должен быть больше 6 и меньше 120.");
        }

        if (loanDetails.getAnnualInterestRate() == null ||
                loanDetails.getAnnualInterestRate().compareTo(BigDecimal.valueOf(3)) <= 0 ||
                loanDetails.getAnnualInterestRate().compareTo(BigDecimal.valueOf(30)) >= 0) {
            throw new IllegalArgumentException("annualInterestRate должен быть больше 3 и меньше 30.");
        }

        try {
            LocalDate loanStartDate = loanDetails.getLoanStartDate();
            DayOfWeek dayOfWeek = loanStartDate.getDayOfWeek();
            if (loanStartDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("loanStartDate не может быть позже текущей даты.");
            }
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || holidayChecker.isHoliday(loanStartDate)) {
                throw new IllegalArgumentException("loanStartDate не должен выпадать на выходной день.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("loanStartDate имеет неверный формат.");
        }
    }
}
