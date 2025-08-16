package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.model.users.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class, ReceiptMapper.class})
public interface UserMapper {

    User toUser(UserJpaEntity userJpaEntity);

    UserJpaEntity toUserJpaEntity(User user, @Context PaymentJpaRepository paymentJpaRepository,
                                  @Context ReceiptJpaRepository receiptJpaRepository);
}
