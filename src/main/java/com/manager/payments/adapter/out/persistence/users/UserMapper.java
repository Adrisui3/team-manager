package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.model.users.User;
import com.manager.payments.model.users.UserMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PaymentMapper.class)
public interface UserMapper {

    User toUser(UserJpaEntity userJpaEntity);

    UserJpaEntity toUserJpaEntity(User user, @Context PaymentJpaRepository paymentJpaRepository);

    UserMinInfo toUserMinInfo(UserJpaEntity userJpaEntity);
}
