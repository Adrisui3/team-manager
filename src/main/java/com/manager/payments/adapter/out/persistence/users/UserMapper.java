package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.model.users.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class, ReceiptMapper.class})
public interface UserMapper {

    User toUser(UserJpaEntity userJpaEntity);

    UserJpaEntity toUserJpaEntity(User user, @Context PaymentJpaRepository paymentJpaRepository,
                                  @Context ReceiptJpaRepository receiptJpaRepository);

    @AfterMapping
    default void linkReceipts(@MappingTarget UserJpaEntity userJpaEntity) {
        if (userJpaEntity.getReceipts() != null) {
            userJpaEntity.getReceipts().forEach(r -> r.setUser(userJpaEntity));
        }
    }
}
