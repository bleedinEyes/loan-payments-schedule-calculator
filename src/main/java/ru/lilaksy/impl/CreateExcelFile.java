package ru.lilaksy.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.lilaksy.domain.PaymentSchedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class CreateExcelFile {
    public static void create(List<PaymentSchedule> paymentSchedule) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Loan Payment Schedule");

        sheet.setColumnWidth(0, 13 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);

        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFont(font);

        String[] headers = {"Payment date", "Payment amount", "Main debt", "Percent"};
        Row headerRow = sheet.createRow(0);

        headerRow.setHeightInPoints(35);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        double totalPaymentAmount = 0;
        double totalPrincipalPayment = 0;
        double totalInterestPayment = 0;

        for (PaymentSchedule payment : paymentSchedule) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(payment.getPaymentDate().toString());
            row.createCell(1).setCellValue(payment.getPaymentAmount());
            row.createCell(2).setCellValue(payment.getPrincipalPayment());
            row.createCell(3).setCellValue(payment.getInterestPayment());

            totalPaymentAmount += payment.getPaymentAmount();
            totalPrincipalPayment += payment.getPrincipalPayment();
            totalInterestPayment += payment.getInterestPayment();
        }

        Row totalRowPlaceholder = sheet.createRow(rowNum);
        totalRowPlaceholder.setHeightInPoints(35);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));

        Cell totalLabelCell = totalRowPlaceholder.createCell(0);
        totalLabelCell.setCellValue("Total:");
        totalLabelCell.setCellStyle(headerStyle);

        Row totalRow = sheet.createRow(++rowNum);
        totalRow.createCell(1).setCellValue(totalPaymentAmount);
        totalRow.createCell(2).setCellValue(totalPrincipalPayment);
        totalRow.createCell(3).setCellValue(totalInterestPayment);

        String directoryPath = "C:\\NSPK_Entry_Tasks";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "\\LoanPaymentSchedule.xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
