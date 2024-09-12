package ru.lilaksy.impl.interfaces;

import ru.lilaksy.domain.PaymentSchedule;

import java.util.List;

public interface FileExporter {
    void create(List<PaymentSchedule> paymentSchedule, String outputFile) throws Exception;
}
