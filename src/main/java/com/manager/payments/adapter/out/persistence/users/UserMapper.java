package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.model.users.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PaymentMapper.class)
public interface UserMapper {

    @Mapping(source = "payments", target = "paymentIds")
    User toUser(UserJpaEntity userJpaEntity);

    @Mapping(source = "paymentIds", target = "payments")
    UserJpaEntity toUserJpaEntity(User user, @Context PaymentJpaRepository paymentJpaRepository);
}
