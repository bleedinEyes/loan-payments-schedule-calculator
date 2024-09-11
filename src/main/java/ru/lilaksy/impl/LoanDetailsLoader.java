package ru.lilaksy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.lilaksy.domain.LoanDetails;

import java.io.InputStream;

public class LoanDetailsLoader {
    public static LoanDetails load() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = LoanDetailsLoader.class.getClassLoader()
                .getResourceAsStream("loanDetails.json");

        if (inputStream == null) {
            throw new IllegalArgumentException("Файл loanDetails.json не найден в папке ресурсов");
        }

        return objectMapper.readValue(inputStream, LoanDetails.class);
    }
}
