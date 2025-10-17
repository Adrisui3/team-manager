package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.players.PlayerMapper;
import com.manager.payments.model.receipts.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, PaymentMapper.class})
public interface ReceiptMapper {

    Receipt toReceipt(ReceiptJpaEntity receiptJpaEntity);

    ReceiptJpaEntity toReceiptJpaEntity(Receipt receipt);

    ReceiptDto toReceiptDto(Receipt receipt);

}
