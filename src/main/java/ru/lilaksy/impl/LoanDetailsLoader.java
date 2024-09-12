package ru.lilaksy.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.lilaksy.domain.LoanDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LoanDetailsLoader {

    public LoanDetails load(String filePath) throws Exception {
        JsonMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            LoanDetails loanDetails = objectMapper.readValue(inputStream, LoanDetails.class);
            validateLoanDetails(loanDetails);
            return loanDetails;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            throw e;
        }
    }

    //======================================================================================================================================================
    //Implementation
    //======================================================================================================================================================

    private void validateLoanDetails(LoanDetails loanDetails) throws IllegalArgumentException {
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
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                throw new IllegalArgumentException("loanStartDate не должен выпадать на выходной день.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("loanStartDate имеет неверный формат.");
        }
    }
}
