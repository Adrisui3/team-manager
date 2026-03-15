package com.manager.payments.application.port.in.receipts;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.MovementGenerator;
import com.manager.payments.generator.ReceiptGenerator;
import com.manager.payments.generator.ReceiptMatchGenerator;
import com.manager.payments.model.exceptions.EmptyFileException;
import com.manager.payments.model.exceptions.InvalidFileFormatException;
import com.manager.payments.model.movements.Movement;
import com.manager.payments.model.movements.ReceiptMatch;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ParseMovementsUseCaseTest {

    @MockitoBean
    private ReceiptRepository repository;

    @Autowired
    private ParseMovementsUseCase parseMovementsUseCase;

    @Captor
    private ArgumentCaptor<Receipt> captor;

    @Test
    void shouldParseAllMovementsAndUpdateReceipts() throws IOException {
        Receipt receipt1 = ReceiptGenerator.receipt().code("12345678-PAY-001-092025").build();
        Receipt receipt2 = ReceiptGenerator.receipt().code("12345678-PAY-001-102025").build();
        Receipt receipt3 = ReceiptGenerator.receipt().code("12345678-PAY-001-112025").build();

        when(repository.findUnpaidByCode("12345678-PAY-001-092025")).thenReturn(Optional.of(receipt1));
        when(repository.findUnpaidByCode("12345678-PAY-001-102025")).thenReturn(Optional.of(receipt2));
        when(repository.findUnpaidByCode("12345678-PAY-001-112025")).thenReturn(Optional.of(receipt3));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ClassPathResource resource = new ClassPathResource("movements-all-match.csv");
        MockMultipartFile file = new MockMultipartFile("file", "movements-all-match.csv", "text/csv",
                resource.getInputStream());

        List<ReceiptMatch> result = parseMovementsUseCase.parseMovements(file);

        BigDecimal amount = BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP);
        LocalDate valueDate = LocalDate.of(2026, 3, 5);

        Movement movement1 = MovementGenerator.movement().concept("12345678-PAY-001-092025").amount(amount).build();
        Movement movement2 = MovementGenerator.movement().concept("12345678-PAY-001-102025").amount(amount).build();
        Movement movement3 = MovementGenerator.movement().concept("12345678-PAY-001-112025").amount(amount).build();

        List<ReceiptMatch> expectedResult = List.of(
                ReceiptMatchGenerator.receiptMatch().receipt(receipt1).movement(movement1).amountCorrect(true).build(),
                ReceiptMatchGenerator.receiptMatch().receipt(receipt2).movement(movement2).amountCorrect(true).build(),
                ReceiptMatchGenerator.receiptMatch().receipt(receipt3).movement(movement3).amountCorrect(true).build()
        );

        assertThat(result).isEqualTo(expectedResult);

        Receipt expectedSaved1 = receipt1.toBuilder().status(ReceiptStatus.PAID).paymentDate(valueDate).build();
        Receipt expectedSaved2 = receipt2.toBuilder().status(ReceiptStatus.PAID).paymentDate(valueDate).build();
        Receipt expectedSaved3 = receipt3.toBuilder().status(ReceiptStatus.PAID).paymentDate(valueDate).build();

        verify(repository, times(3)).save(captor.capture());
        assertThat(captor.getAllValues()).isEqualTo(List.of(expectedSaved1, expectedSaved2, expectedSaved3));
    }

    @Test
    void shouldUpdateOnlyOneReceipt() throws IOException {
        Receipt receipt1 = ReceiptGenerator.receipt().code("12345678-PAY-001-092025").build();
        Receipt receipt2 = ReceiptGenerator.receipt().code("12345678-PAY-001-102025").build();
        Receipt receipt3 = ReceiptGenerator.receipt().code("12345678-PAY-001-112025").build();

        when(repository.findUnpaidByCode("12345678-PAY-001-092025")).thenReturn(Optional.of(receipt1));
        when(repository.findUnpaidByCode("12345678-PAY-001-102025")).thenReturn(Optional.of(receipt2));
        when(repository.findUnpaidByCode("12345678-PAY-001-112025")).thenReturn(Optional.of(receipt3));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ClassPathResource resource = new ClassPathResource("movements-one-correct.csv");
        MockMultipartFile file = new MockMultipartFile("file", "movements-one-correct.csv", "text/csv",
                resource.getInputStream());

        List<ReceiptMatch> result = parseMovementsUseCase.parseMovements(file);

        LocalDate valueDate = LocalDate.of(2026, 3, 5);

        Movement movement1 =
                MovementGenerator.movement().concept("12345678-PAY-001-092025").amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP)).build();
        Movement movement2 =
                MovementGenerator.movement().concept("12345678-PAY-001-102025").amount(BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_UP)).build();
        Movement movement3 =
                MovementGenerator.movement().concept("12345678-PAY-001-112025").amount(BigDecimal.valueOf(40).setScale(2, RoundingMode.HALF_UP)).build();

        List<ReceiptMatch> expectedResult = List.of(
                ReceiptMatchGenerator.receiptMatch().receipt(receipt1).movement(movement1).amountCorrect(true).build(),
                ReceiptMatchGenerator.receiptMatch().receipt(receipt2).movement(movement2).amountCorrect(false).build(),
                ReceiptMatchGenerator.receiptMatch().receipt(receipt3).movement(movement3).amountCorrect(false).build()
        );

        assertThat(result).isEqualTo(expectedResult);

        Receipt expectedSaved = receipt1.toBuilder().status(ReceiptStatus.PAID).paymentDate(valueDate).build();

        verify(repository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedSaved);
    }

    @Test
    void shouldFailWhenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile("file", "movements.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> parseMovementsUseCase.parseMovements(file))
                .isInstanceOf(EmptyFileException.class);
    }

    @Test
    void shouldFailWhenFileIsNull() {
        MockMultipartFile file = new MockMultipartFile("file", null, "text/csv", "content".getBytes());

        assertThatThrownBy(() -> parseMovementsUseCase.parseMovements(file))
                .isInstanceOf(InvalidFileFormatException.class);
    }

    @Test
    void shouldFailWhenFormatIsNotCsv() {
        MockMultipartFile file = new MockMultipartFile("file", "movements.json", "application/json", "{}".getBytes());

        assertThatThrownBy(() -> parseMovementsUseCase.parseMovements(file))
                .isInstanceOf(InvalidFileFormatException.class);
    }
}
