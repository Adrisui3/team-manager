package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaRepository;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.users.PlayerMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toPayment(PaymentJpaEntity paymentJpaEntity);

    PaymentJpaEntity toPaymentJpaEntity(Payment payment, @Context PlayerJpaRepository playerJpaRepository);

    PaymentMinInfo toPaymentMinInfo(PaymentJpaEntity paymentJpaEntity);

    default List<PaymentJpaEntity> mapPaymentMinInfosToPaymentJpaEntities(List<PaymentMinInfo> paymentMinInfos,
                                                                          @Context PaymentJpaRepository paymentJpaRepository) {
        return paymentMinInfos.stream().map(payment -> mapPaymentMinInfoToPaymentJpaEntity(payment,
                paymentJpaRepository)).toList();
    }

    default PaymentJpaEntity mapPaymentMinInfoToPaymentJpaEntity(PaymentMinInfo paymentMinInfo,
                                                                 @Context PaymentJpaRepository paymentJpaRepository) {
        return paymentJpaRepository.findById(paymentMinInfo.id()).orElseThrow(() -> new PaymentNotFoundException(paymentMinInfo.id()));
    }

    default List<PlayerJpaEntity> mapPlayerMinInfosToPlayerJpaEntities(List<PlayerMinInfo> playerMinInfos,
                                                                       @Context PlayerJpaRepository playerJpaRepository) {
        return playerMinInfos.stream().map(player -> mapPlayerMinInfoToPlayerJpaEntity(player, playerJpaRepository)).toList();
    }

    default PlayerJpaEntity mapPlayerMinInfoToPlayerJpaEntity(PlayerMinInfo playerMinInfo,
                                                              @Context PlayerJpaRepository playerJpaRepository) {
        return playerJpaRepository.findById(playerMinInfo.id()).orElseThrow(() -> new PlayerNotFoundException(playerMinInfo.id()));
    }
}
