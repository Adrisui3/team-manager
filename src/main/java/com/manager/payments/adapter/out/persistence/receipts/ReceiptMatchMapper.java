package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.in.rest.dto.models.ReceiptMatchDto;
import com.manager.payments.model.movements.ReceiptMatch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReceiptMapper.class})
public interface ReceiptMatchMapper {

    ReceiptMatchDto toReceiptMatchDto(ReceiptMatch receiptMatch);

}
