package impl.helpers;

import org.junit.jupiter.api.Test;
import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentType;
import ru.lilaksy.impl.helpers.LoanDetailsValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanDetailsValidatorTest {

    private final static LoanDetails NEGATIVE_LOAN_AMOUNT = new LoanDetails(
            new BigDecimal("-10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2023, 9, 1),
            PaymentType.ANNUITY
    );

    private final static LoanDetails WRONG_LOAN_TERM = new LoanDetails(
            new BigDecimal("10000"),
            5,
            new BigDecimal("12"),
            LocalDate.of(2023, 9, 1),
            PaymentType.ANNUITY
    );

    private final static LoanDetails WRONG_INTEREST_RATE = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("2"),
            LocalDate.of(2023, 9, 1),
            PaymentType.ANNUITY
    );

    private final static LoanDetails LOAN_WITH_START_DAY_SATURDAY = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2023, 9, 2),
            PaymentType.ANNUITY
    );

    private final static LoanDetails LOAN_WITH_WRONG_START_DAY = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2124, 9, 5),
            PaymentType.ANNUITY
    );

    private final LoanDetailsValidator validator = new LoanDetailsValidator();

    @Test
    void testValidateWithNegativeLoanAmount() {

        assertThatThrownBy(() -> validator.validateLoanDetails(NEGATIVE_LOAN_AMOUNT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("loanAmount должен быть больше 0.");
    }

    @Test
    void testValidateWithInvalidLoanTerm() {

        assertThatThrownBy(() -> validator.validateLoanDetails(WRONG_LOAN_TERM))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("loanTermMonths должен быть больше 6 и меньше 120.");
    }

    @Test
    void testValidateWithInvalidInterestRate() {

        assertThatThrownBy(() -> validator.validateLoanDetails(WRONG_INTEREST_RATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("annualInterestRate должен быть больше 3 и меньше 30.");
    }

    @Test
    void testValidateWithWeekendLoanStartDate() {

        assertThatThrownBy(() -> validator.validateLoanDetails(LOAN_WITH_START_DAY_SATURDAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("loanStartDate не должен выпадать на выходной день.");
    }

    @Test
    void testValidateWithInvalidDateFormat() {

        assertThatThrownBy(() -> validator.validateLoanDetails(LOAN_WITH_WRONG_START_DAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("loanStartDate не может быть позже текущей даты.");
    }
}
