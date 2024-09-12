package impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lilaksy.domain.LoanDetails;
import ru.lilaksy.domain.PaymentType;
import ru.lilaksy.impl.LoanDetailsLoaderJson;
import ru.lilaksy.impl.helpers.LoanDetailsValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanDetailsLoaderJsonTest {

    private final static LoanDetails CORRECT_LOAN_DETAILS = new LoanDetails(
            new BigDecimal("10000"),
            12,
            new BigDecimal("12"),
            LocalDate.of(2023, 9, 1),
            PaymentType.ANNUITY
    );

    private final static String CORRECT_FILE_PATH_TO_CORRECT_FILE = "src/test/resources/correctLoanDetails.json";
    private final static String INVALID_FILE_PATH = "invalid/file/path/some.json";
    private final static String CORRECT_FILE_PATH_TO_INVALID_FILE = "src/test/resources/invalidLoanDetails.json";

    @Mock
    private LoanDetailsValidator validatorMock;

    @InjectMocks
    private LoanDetailsLoaderJson loader;

    @Test
    void testLoadSuccess() throws Exception {

        LoanDetails actualLoanDetails = loader.load(CORRECT_FILE_PATH_TO_CORRECT_FILE);

        assertThat(actualLoanDetails).isEqualTo(CORRECT_LOAN_DETAILS);

        verify(validatorMock).validateLoanDetails(actualLoanDetails);
    }

    @Test
    void testLoadFileNotFound() {

        assertThatThrownBy(() -> loader.load(INVALID_FILE_PATH))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("some.json");

        verify(validatorMock, never()).validateLoanDetails(any());
    }

    @Test
    void testLoadValidationFailure(){

        doThrow(new IllegalArgumentException("loanAmount должен быть больше 0."))
                .when(validatorMock).validateLoanDetails(any());

        assertThatThrownBy(() -> loader.load(CORRECT_FILE_PATH_TO_INVALID_FILE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("loanAmount должен быть больше 0.");

        verify(validatorMock, times(1)).validateLoanDetails(any());
    }
}