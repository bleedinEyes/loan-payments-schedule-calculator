package ru.lilaksy.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.impl.interfaces.FileExporter;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class ExcelFileExporter implements FileExporter {

    @Override
    public void create(List<PaymentSchedule> paymentSchedule, String outputFile) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            XSSFSheet sheet = workbook.createSheet("Loan Payment Schedule");

            sheet.setColumnWidth(0, 13 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);

            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {"Payment date", "Payment amount", "Main debt", "Percent"};
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(35);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            BigDecimal totalPaymentAmount = BigDecimal.ZERO;
            BigDecimal totalPrincipalPayment = BigDecimal.ZERO;
            BigDecimal totalInterestPayment = BigDecimal.ZERO;

            for (PaymentSchedule payment : paymentSchedule) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(payment.getPaymentDate().toString());
                row.createCell(1).setCellValue(payment.getPaymentAmount().doubleValue());
                row.createCell(2).setCellValue(payment.getPrincipalPayment().doubleValue());
                row.createCell(3).setCellValue(payment.getInterestPayment().doubleValue());

                totalPaymentAmount = totalPaymentAmount.add(payment.getPaymentAmount());
                totalPrincipalPayment = totalPrincipalPayment.add(payment.getPrincipalPayment());
                totalInterestPayment = totalInterestPayment.add(payment.getInterestPayment());
            }

            createTotalRow(sheet, headerStyle, rowNum, totalPaymentAmount, totalPrincipalPayment, totalInterestPayment);

            workbook.write(outputStream);
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(font);

        return headerStyle;
    }

    private void createTotalRow(XSSFSheet sheet, CellStyle headerStyle, int rowNum,
                                BigDecimal totalPaymentAmount, BigDecimal totalPrincipalPayment,
                                BigDecimal totalInterestPayment) {
        Row totalRowPlaceholder = sheet.createRow(rowNum);
        totalRowPlaceholder.setHeightInPoints(35);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));

        Cell totalLabelCell = totalRowPlaceholder.createCell(0);
        totalLabelCell.setCellValue("Total:");
        totalLabelCell.setCellStyle(headerStyle);

        Row totalRow = sheet.createRow(++rowNum);
        totalRow.createCell(1).setCellValue(totalPaymentAmount.doubleValue());
        totalRow.createCell(2).setCellValue(totalPrincipalPayment.doubleValue());
        totalRow.createCell(3).setCellValue(totalInterestPayment.doubleValue());
    }
}

