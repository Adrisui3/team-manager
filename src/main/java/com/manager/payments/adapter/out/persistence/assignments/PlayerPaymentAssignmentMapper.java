package com.manager.payments.adapter.out.persistence.assignments;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlayerPaymentAssignmentMapper {

    PlayerPaymentAssignment toPlayerPaymentAssignment(PlayerPaymentAssignmentJpaEntity entity);

    PlayerPaymentAssignmentJpaEntity toPlayerPaymentAssignmentJpaEntity(PlayerPaymentAssignment entity);

    @Mapping(target = "receipts", ignore = true)
    void updateEntity(PlayerPaymentAssignment playerPaymentAssignment,
                      @MappingTarget PlayerPaymentAssignmentJpaEntity playerPaymentAssignmentJpaEntity);
}
