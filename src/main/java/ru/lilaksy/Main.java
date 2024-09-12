package ru.lilaksy;

import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.impl.PaymentScheduleCalculator;
import ru.lilaksy.impl.ExcelFileExporter;
import ru.lilaksy.impl.LoanDetailsLoaderJson;
import ru.lilaksy.impl.interfaces.FileExporter;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java -jar build/libs/your-fat-jar-1.0.jar <input file path> <output file path>");
            System.exit(1);
        }

        LoanDetailsLoaderJson loader = new LoanDetailsLoaderJson();
        PaymentScheduleCalculator calculator = new PaymentScheduleCalculator();
        FileExporter exporter = new ExcelFileExporter();

        try {
            LoanDetails loanDetails = loader.load(args[0]);
            List<PaymentSchedule> paymentSchedule = calculator.calculate(loanDetails);
            exporter.create(paymentSchedule, args[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}