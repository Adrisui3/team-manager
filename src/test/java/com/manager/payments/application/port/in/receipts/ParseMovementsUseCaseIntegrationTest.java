package com.manager.payments.application.port.in.receipts;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.generator.ReceiptGenerator;
import com.manager.payments.model.exceptions.EmptyFileException;
import com.manager.payments.model.exceptions.InvalidFileFormatException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ParseMovementsUseCaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ParseMovementsUseCase parseMovementsUseCase;

    @Test
    void shouldParseAllMovementsAndUpdateReceipts() throws IOException {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());

        Receipt saved1 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-092025").player(player).payment(payment).build());
        Receipt saved2 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-102025").player(player).payment(payment).build());
        Receipt saved3 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-112025").player(player).payment(payment).build());

        ClassPathResource resource = new ClassPathResource("movements-all-match.csv");
        MockMultipartFile file = new MockMultipartFile("file", "movements-all-match.csv", "text/csv",
                resource.getInputStream());

        parseMovementsUseCase.parseMovements(file);

        LocalDate expectedPaymentDate = LocalDate.of(2026, 3, 5);
        assertThat(receiptRepository.findById(saved1.id())).hasValueSatisfying(r -> {
            assertThat(r.status()).isEqualTo(ReceiptStatus.PAID);
            assertThat(r.paymentDate()).isEqualTo(expectedPaymentDate);
        });
        assertThat(receiptRepository.findById(saved2.id())).hasValueSatisfying(r -> {
            assertThat(r.status()).isEqualTo(ReceiptStatus.PAID);
            assertThat(r.paymentDate()).isEqualTo(expectedPaymentDate);
        });
        assertThat(receiptRepository.findById(saved3.id())).hasValueSatisfying(r -> {
            assertThat(r.status()).isEqualTo(ReceiptStatus.PAID);
            assertThat(r.paymentDate()).isEqualTo(expectedPaymentDate);
        });
    }

    @Test
    void shouldUpdateOnlyOneReceipt() throws IOException {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());

        Receipt saved1 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-092025").player(player).payment(payment).build());
        Receipt saved2 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-102025").player(player).payment(payment).build());
        Receipt saved3 = receiptRepository.save(ReceiptGenerator.receipt()
                .code("12345678-PAY-001-112025").player(player).payment(payment).build());

        ClassPathResource resource = new ClassPathResource("movements-one-correct.csv");
        MockMultipartFile file = new MockMultipartFile("file", "movements-one-correct.csv", "text/csv",
                resource.getInputStream());

        parseMovementsUseCase.parseMovements(file);

        assertThat(receiptRepository.findById(saved1.id())).hasValueSatisfying(r ->
                assertThat(r.status()).isEqualTo(ReceiptStatus.PAID));
        assertThat(receiptRepository.findById(saved2.id())).hasValueSatisfying(r ->
                assertThat(r.status()).isEqualTo(ReceiptStatus.PENDING));
        assertThat(receiptRepository.findById(saved3.id())).hasValueSatisfying(r ->
                assertThat(r.status()).isEqualTo(ReceiptStatus.PENDING));
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
