package com.manager.email.adapter.out.persistence;


import com.manager.email.model.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    Email toDomain(EmailOutboxJpaEntity entity);

    EmailOutboxJpaEntity toEntity(Email email);
}
