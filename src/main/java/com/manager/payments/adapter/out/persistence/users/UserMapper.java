package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.model.users.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserJpaEntity userJpaEntity);

    UserJpaEntity toUserJpaEntity(User user);
}
