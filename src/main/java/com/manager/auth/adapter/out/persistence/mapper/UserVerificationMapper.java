package com.manager.auth.adapter.out.persistence.mapper;

import com.manager.auth.adapter.out.persistence.users.UserVerificationJpaEntity;
import com.manager.auth.model.users.UserVerification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserVerificationMapper {

    @Mapping(target = "userId", source = "user.id")
    UserVerification toUserVerification(UserVerificationJpaEntity userVerificationJpaEntity);

    @Mapping(target = "user", ignore = true)
    UserVerificationJpaEntity toUserVerificationJpaEntity(UserVerification userVerification);

}
