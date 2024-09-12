package impl;

import org.junit.jupiter.api.Test;
import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentSchedule;
import ru.lilaksy.domain.PaymentType;
import ru.lilaksy.impl.PaymentScheduleCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PaymentScheduleCalculatorTest {

    private static final LoanDetails CORRECT_LOAN_DETAILS_ANNUITY = new LoanDetails(
                new BigDecimal("10000"),
                12,
                new BigDecimal("12"),
                LocalDate.of(2023, 9, 1),
                PaymentType.ANNUITY
        );

    private static final LoanDetails CORRECT_LOAN_DETAILS_DIFFERENTIATED = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2023, 9, 1),
            PaymentType.DIFFERENTIATED
    );

    private static final LoanDetails LOAN_DETAILS_HOLIDAY_START = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2024, 9, 7),
            PaymentType.ANNUITY
    );

    private final PaymentScheduleCalculator calculator = new PaymentScheduleCalculator();

    @Test
    void testCalculateAnnuityPaymentSchedule() {

        List<PaymentSchedule> schedule = calculator.calculate(CORRECT_LOAN_DETAILS_ANNUITY);

        assertThat(schedule).hasSize(12);

        PaymentSchedule firstPayment = schedule.get(0);
        assertThat(firstPayment.getPaymentDate()).isEqualTo(LocalDate.of(2023, 9, 1));
        assertThat(firstPayment.getPaymentAmount()).isEqualByComparingTo(new BigDecimal("888.49"));

        PaymentSchedule lastPayment = schedule.get(schedule.size() - 1);
        assertThat(lastPayment.getPaymentDate()).isEqualTo(LocalDate.of(2024, 8, 1));
        assertThat(lastPayment.getPaymentAmount()).isEqualByComparingTo(new BigDecimal("888.49"));
    }

    @Test
    void testCalculateDifferentiatedPaymentSchedule() {

        List<PaymentSchedule> schedule = calculator.calculate(CORRECT_LOAN_DETAILS_DIFFERENTIATED);

        assertThat(schedule).hasSize(12);

        PaymentSchedule firstPayment = schedule.get(0);
        assertThat(firstPayment.getPaymentDate()).isEqualTo(LocalDate.of(2023, 9, 1));
        assertThat(firstPayment.getPaymentAmount()).isEqualByComparingTo(new BigDecimal("933.33"));

        PaymentSchedule lastPayment = schedule.get(schedule.size() - 1);
        assertThat(lastPayment.getPaymentDate()).isEqualTo(LocalDate.of(2024, 8, 1));
        assertThat(lastPayment.getPaymentAmount()).isEqualByComparingTo(new BigDecimal("841.66"));
    }

    @Test
    void testHolidayAdjustment() {

        List<PaymentSchedule> schedule = calculator.calculate(LOAN_DETAILS_HOLIDAY_START);

        assertThat(schedule.get(0).getPaymentDate()).isEqualTo(LocalDate.of(2024, 9, 9));
    }

    @Test
    void testLastPaymentCorrection() {

        List<PaymentSchedule> schedule = calculator.calculate(CORRECT_LOAN_DETAILS_ANNUITY);

        BigDecimal totalPrincipalPaid = schedule.stream()
                .map(PaymentSchedule::getPrincipalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(totalPrincipalPaid).isEqualByComparingTo(new BigDecimal("10000.00"));
    }
}
