package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.application.exception.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    Receipt toReceipt(ReceiptJpaEntity receiptJpaEntity);

    ReceiptJpaEntity toReceiptJpaEntity(Receipt receipt);

    ReceiptMinInfo toReceiptMinInfo(ReceiptJpaEntity receiptJpaEntity);

    default List<ReceiptJpaEntity> mapReceiptMinInfosToReceiptJpaEntities(List<ReceiptMinInfo> receiptMinInfos,
                                                                          @Context ReceiptJpaRepository receiptJpaRepository) {
        return receiptMinInfos.stream().map(receipt -> mapReceiptMinInfoToReceiptJpaEntity(receipt,
                receiptJpaRepository)).toList();
    }

    default ReceiptJpaEntity mapReceiptMinInfoToReceiptJpaEntity(ReceiptMinInfo receiptMinInfo,
                                                                 @Context ReceiptJpaRepository receiptJpaRepository) {
        return receiptJpaRepository.findById(receiptMinInfo.id()).orElseThrow(() -> new ReceiptNotFoundException(receiptMinInfo.id()));
    }
}
