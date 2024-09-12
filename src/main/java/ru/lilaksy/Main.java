package ru.lilaksy;

import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.impl.PaymentScheduleCalculator;
import ru.lilaksy.impl.ExcelFileExporter;
import ru.lilaksy.impl.LoanDetailsLoader;
import ru.lilaksy.impl.interfaces.FileExporter;

import java.util.List;


public class Main {
    public static void main(String[] args) {

//        if (args.length < 2) {
//            System.out.println("Usage: java -jar build/libs/nspk-credit-payments-1.0-SNAPSHOT.jar <input file> <output file>");
//            System.exit(1);
//        }

        LoanDetailsLoader loader = new LoanDetailsLoader();
        PaymentScheduleCalculator calculator = new PaymentScheduleCalculator();
        FileExporter exporter = new ExcelFileExporter();

        //java -jar build/libs/nspk-credit-payments-1.0-SNAPSHOT.jar C:\Users\lilak\Desktop\JAVA\nspk-credit-payments\src\main\resources\loanDetails.json C:\NSPK_Entry_Tasks\test.xlsx
        try {
            LoanDetails loanDetails = loader.load("C:\\Users\\lilak\\Desktop\\JAVA\\nspk-credit-payments\\src\\main\\resources\\loanDetails.json");

            List<PaymentSchedule> paymentSchedule = calculator.calculate(loanDetails);

            exporter.create(paymentSchedule, "C:\\NSPK_Entry_Tasks\\test.xlsx");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}