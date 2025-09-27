package com.manager.payments.model.billing;

import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerMinInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class BillingProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BillingProcessor.class);

    private BillingProcessor() {
    }

    public static Payment process(Payment payment, LocalDate date,
                                  Function<UUID, Optional<Player>> findPlayer,
                                  Function<Player, Player> persistPlayer) {
        LocalDate newNextPaymentDate = payment.nextPaymentDate();
        while (isBillable(newNextPaymentDate, date, payment.endDate())) {
            Payment updatedPayment = payment.withNextPaymentDate(newNextPaymentDate);
            for (PlayerMinInfo player : updatedPayment.players()) {
                Player completePlayer =
                        findPlayer.apply(player.id()).orElseThrow(() -> new PlayerNotFoundException(player.id()));
                completePlayer.createReceiptFor(payment);
                persistPlayer.apply(completePlayer);
                logger.info("Created receipt {} for player {}", completePlayer.receipts().getLast().id(),
                        completePlayer.id());
            }

            newNextPaymentDate = newNextPaymentDate.plusDays(payment.periodDays());
        }

        return payment.withNextPaymentDate(newNextPaymentDate);
    }

    private static boolean isBillable(LocalDate nextPaymentDate, LocalDate date, LocalDate endDate) {
        return (nextPaymentDate.isBefore(date) || nextPaymentDate.isEqual(date)) && nextPaymentDate.isBefore(endDate);
    }
}
