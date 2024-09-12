package ru.lilaksy.impl.interfaces;

import ru.lilaksy.domain.LoanDetails;

public interface LoanDetailsLoader {
    LoanDetails load(String filePath) throws Exception;
}
