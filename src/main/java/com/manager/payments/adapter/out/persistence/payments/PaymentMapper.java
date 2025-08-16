package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.adapter.out.persistence.users.UserJpaEntity;
import com.manager.payments.adapter.out.persistence.users.UserJpaRepository;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.users.UserMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toPayment(PaymentJpaEntity paymentJpaEntity);

    PaymentJpaEntity toPaymentJpaEntity(Payment payment, @Context UserJpaRepository userJpaRepository);

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

    default List<UserJpaEntity> mapUserMinInfosToUserJpaEntities(List<UserMinInfo> userMinInfos,
                                                                 @Context UserJpaRepository userJpaRepository) {
        return userMinInfos.stream().map(user -> mapUserMinInfoToUserJpaEntity(user, userJpaRepository)).toList();
    }

    default UserJpaEntity mapUserMinInfoToUserJpaEntity(UserMinInfo userMinInfo, @Context UserJpaRepository userJpaRepository) {
        return userJpaRepository.findById(userMinInfo.id()).orElseThrow(() -> new UserNotFoundException(userMinInfo.id()));
    }
}
