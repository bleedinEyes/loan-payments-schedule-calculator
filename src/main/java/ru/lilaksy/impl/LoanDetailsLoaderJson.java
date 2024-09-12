package ru.lilaksy.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.impl.helpers.LoanDetailsValidator;
import ru.lilaksy.impl.interfaces.LoanDetailsLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoanDetailsLoaderJson implements LoanDetailsLoader {

    LoanDetailsValidator validator = new LoanDetailsValidator();

    @Override
    public LoanDetails load(String filePath) throws Exception {
        JsonMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            LoanDetails loanDetails = objectMapper.readValue(inputStream, LoanDetails.class);
            validator.validateLoanDetails(loanDetails);
            return loanDetails;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            throw e;
        }
    }
}
