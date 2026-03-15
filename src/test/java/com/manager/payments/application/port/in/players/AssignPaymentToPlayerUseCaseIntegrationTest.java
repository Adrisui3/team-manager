package com.manager.payments.application.port.in.players;

import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.generator.PaymentGenerator;
import com.manager.payments.generator.PlayerGenerator;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.exceptions.AssignmentAlreadyExistsException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AssignPaymentToPlayerUseCaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;

    @Autowired
    private AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;

    @Test
    void shouldAssignPaymentToPlayer() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());

        assignPaymentToPlayerUseCase.assignPaymentToPlayer(player.id(), payment.id());

        assertThat(playerPaymentAssignmentRepository.existsByPlayerIdAndPaymentId(player.id(), payment.id())).isTrue();
    }

    @Test
    void shouldFailIfAssignmentAlreadyExists() {
        Player player = playerRepository.save(PlayerGenerator.player().id(null).build());
        Payment payment = paymentRepository.save(PaymentGenerator.payment().id(null).build());
        playerPaymentAssignmentRepository.save(PlayerPaymentAssignment.builder()
                .player(player)
                .payment(payment)
                .build());

        assertThatThrownBy(() -> assignPaymentToPlayerUseCase.assignPaymentToPlayer(player.id(), payment.id()))
                .isInstanceOf(AssignmentAlreadyExistsException.class);
    }
}
