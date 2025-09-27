package com.manager.payments.model.players;

import com.manager.payments.model.exceptions.PlayerPaymentAssignmentInconsistent;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.payments.PaymentStatus;

public class PlayerPaymentAssigner {

    private PlayerPaymentAssigner() {
    }

    public static boolean assign(Player player, Payment payment) {
        if (player.hasPayment(payment.id()) != payment.hasPlayer(player.id())) {
            throw new PlayerPaymentAssignmentInconsistent(payment.id(), player.id());
        }

        if (!player.hasPayment(payment.id()) && !payment.hasPlayer(player.id())) {
            PaymentMinInfo paymentMinInfo = PaymentMinInfo.from(payment);
            player.payments().add(paymentMinInfo);

            PlayerMinInfo playerMinInfo = PlayerMinInfo.from(player);
            payment.players().add(playerMinInfo);

            if (payment.status().equals(PaymentStatus.ACTIVE)) {
                player.createReceiptFor(payment);
            }

            return true;
        }

        return false;
    }
}
