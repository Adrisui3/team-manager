package com.manager.payments.model.assignments;

import com.manager.payments.model.exceptions.DisabledPlayerException;
import com.manager.payments.model.exceptions.PaymentExpiredException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;

public class PlayerPaymentAssignmentFactory {

    private PlayerPaymentAssignmentFactory() {
    }

    public static PlayerPaymentAssignment build(Player player, Payment payment) {
        if (player.status() == PlayerStatus.DISABLED) {
            throw new DisabledPlayerException();
        }

        if (payment.status() == PaymentStatus.EXPIRED) {
            throw new PaymentExpiredException();
        }

        return new PlayerPaymentAssignment(player, payment);
    }

}
