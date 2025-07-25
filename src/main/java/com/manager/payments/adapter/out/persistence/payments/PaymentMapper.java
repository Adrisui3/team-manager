package com.manager.payments.adapter.out.persistence.payments;

import com.manager.payments.model.payments.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentJpaEntity paymentJpaEntity);

    PaymentJpaEntity toPaymentJpaEntity(Payment payment);
}
