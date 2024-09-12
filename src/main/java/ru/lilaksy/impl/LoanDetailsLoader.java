package ru.lilaksy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.lilaksy.domain.LoanDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoanDetailsLoader {
    private static final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    public LoanDetails load(String filePath) throws Exception {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return objectMapper.readValue(inputStream, LoanDetails.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            throw e;
        }
    }
}
