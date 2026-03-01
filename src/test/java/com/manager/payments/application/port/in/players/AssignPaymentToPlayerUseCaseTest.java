package com.manager.payments.application.port.in.players;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.model.exceptions.AssignmentAlreadyExistsException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AssignPaymentToPlayerUseCaseTest {

    @MockitoBean
    private PlayerRepository playerRepository;

    @MockitoBean
    private PaymentRepository paymentRepository;

    @MockitoBean
    private PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;

    @MockitoBean
    private ReceiptRepository receiptRepository;

    @Autowired
    private AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;

    @Test
    void shouldAssignPaymentToPlayer() {
        UUID playerId = UUID.randomUUID();
        Player player = PlayerGenerator.player().id(playerId).build();
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = PaymentGenerator.payment().id(paymentId).build();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        when(playerPaymentAssignmentRepository.existsByPlayerIdAndPaymentId(playerId, paymentId)).thenReturn(false);
        when(playerPaymentAssignmentRepository.save(any())).then(returnsFirstArg());

        assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);

        verify(playerPaymentAssignmentRepository).save(any());
    }

    @Test
    void shouldFailIfAssignmentAlreadyExists() {
        UUID playerId = UUID.randomUUID();
        Player player = PlayerGenerator.player().id(playerId).build();
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        UUID paymentId = UUID.randomUUID();
        Payment payment = PaymentGenerator.payment().id(paymentId).build();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        when(playerPaymentAssignmentRepository.existsByPlayerIdAndPaymentId(playerId, paymentId)).thenReturn(true);

        assertThatThrownBy(() -> assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId))
                .isInstanceOf(AssignmentAlreadyExistsException.class);
    }
}
