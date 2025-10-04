package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.adapter.in.rest.dto.models.PlayerPaymentAssignmentDto;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerPaymentAssignmentMapper {

    PlayerPaymentAssignment toPlayerPaymentAssignment(PlayerPaymentAssignmentJpaEntity entity);

    PlayerPaymentAssignmentJpaEntity toPlayerPaymentAssignmentJpaEntity(PlayerPaymentAssignment playerPaymentAssignment);

    PlayerPaymentAssignmentDto toPlayerPaymentAssignmentDto(PlayerPaymentAssignment playerPaymentAssignment);
}
