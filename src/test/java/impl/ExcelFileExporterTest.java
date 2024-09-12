package impl;

import org.junit.jupiter.api.Test;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.impl.ExcelFileExporter;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelFileExporterTest {
    @Test
    void testCreateExcelFileWithTempFile() throws Exception {
        List<PaymentSchedule> schedule = List.of(
                new PaymentSchedule(LocalDate.of(2023, 9, 1), new BigDecimal("1000"), new BigDecimal("800"), new BigDecimal("200")),
                new PaymentSchedule(LocalDate.of(2023, 10, 1), new BigDecimal("1000"), new BigDecimal("800"), new BigDecimal("200"))
        );

        File tempFile = Files.createTempFile("loan_schedule", ".xlsx").toFile();

        ExcelFileExporter exporter = new ExcelFileExporter();

        exporter.create(schedule, tempFile.getAbsolutePath());

        assertThat(tempFile).exists();
        assertThat(tempFile.length()).isGreaterThan(0);

        tempFile.deleteOnExit();
    }
}
