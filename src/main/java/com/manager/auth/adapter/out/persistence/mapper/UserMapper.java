package com.manager.auth.adapter.out.persistence.mapper;

import com.manager.auth.adapter.in.rest.dto.models.UserDto;
import com.manager.auth.adapter.out.persistence.users.UserJpaEntity;
import com.manager.auth.model.users.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserVerificationMapper.class})
public interface UserMapper {

    User toUser(UserJpaEntity userJpaEntity);

    UserJpaEntity toUserJpaEntity(User user);

    UserDto toUserDto(User user);

    @AfterMapping
    default void linkVerification(@MappingTarget UserJpaEntity userEntity) {
        if (userEntity.getVerification() != null) {
            userEntity.getVerification().setUser(userEntity);
        }
    }
}
