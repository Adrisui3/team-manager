package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.adapter.out.persistence.users.UserJpaEntity;
import com.manager.payments.adapter.out.persistence.users.UserJpaRepository;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.model.payments.Payment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "users", target = "userIds")
    Payment toPayment(PaymentJpaEntity paymentJpaEntity);

    @Mapping(source = "userIds", target = "users")
    PaymentJpaEntity toPaymentJpaEntity(Payment payment, @Context UserJpaRepository userJpaRepository);

    default List<PaymentJpaEntity> mapIdsToPaymentJpaEntities(List<UUID> paymentIds, @Context PaymentJpaRepository paymentJpaRepository) {
        return paymentIds.stream().map(id -> mapIdToPaymentJpaEntity(id, paymentJpaRepository)).toList();
    }

    default PaymentJpaEntity mapIdToPaymentJpaEntity(UUID paymentId, @Context PaymentJpaRepository paymentJpaRepository) {
        return paymentJpaRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    default List<UUID> mapPaymentJpaEntitiesToIds(List<PaymentJpaEntity> paymentJpaEntities) {
        return paymentJpaEntities.stream().map(PaymentJpaEntity::getId).collect(Collectors.toList());
    }

    default List<UserJpaEntity> mapIdsToUserJpaEntities(List<UUID> userIds, @Context UserJpaRepository userJpaRepository) {
        return userIds.stream().map(id -> mapIdToUserJpaEntity(id, userJpaRepository)).toList();
    }

    default UserJpaEntity mapIdToUserJpaEntity(UUID userId, @Context UserJpaRepository userJpaRepository) {
        return userJpaRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    default List<UUID> mapUserJpaEntitiesToIds(List<UserJpaEntity> userJpaEntities) {
        return userJpaEntities.stream().map(UserJpaEntity::getId).collect(Collectors.toList());
    }
}
