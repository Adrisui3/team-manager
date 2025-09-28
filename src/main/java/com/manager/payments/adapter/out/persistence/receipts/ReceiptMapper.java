package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentMapper;
import com.manager.payments.model.receipts.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PlayerPaymentAssignmentMapper.class)
public interface ReceiptMapper {

    Receipt toReceipt(ReceiptJpaEntity receiptJpaEntity);

    ReceiptJpaEntity toReceiptJpaEntity(Receipt receipt);

}
