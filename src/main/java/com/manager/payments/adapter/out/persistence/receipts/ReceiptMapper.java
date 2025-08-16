package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.out.persistence.users.UserJpaEntity;
import com.manager.payments.adapter.out.persistence.users.UserJpaRepository;
import com.manager.payments.application.exception.ReceiptNotFoundException;
import com.manager.payments.application.exception.UserNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import com.manager.payments.model.users.UserMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    Receipt toReceipt(ReceiptJpaEntity receiptJpaEntity);

    ReceiptJpaEntity toReceiptJpaEntity(Receipt receipt, @Context UserJpaRepository userJpaRepository);

    ReceiptMinInfo toReceiptMinInfo(ReceiptJpaEntity receiptJpaEntity);

    default List<ReceiptJpaEntity> mapReceiptMinInfosToReceiptJpaEntities(List<ReceiptMinInfo> receiptMinInfos,
                                                                          @Context ReceiptJpaRepository receiptJpaRepository) {
        return receiptMinInfos.stream().map(receipt -> mapReceiptMinInfoToReceiptJpaEntity(receipt,
                receiptJpaRepository)).toList();
    }

    default ReceiptJpaEntity mapReceiptMinInfoToReceiptJpaEntity(ReceiptMinInfo receiptMinInfo,
                                                                 @Context ReceiptJpaRepository receiptJpaRepository) {
        if (receiptMinInfo.id() == null) {
            ReceiptJpaEntity newReceipt = new ReceiptJpaEntity();
            newReceipt.setAmount(receiptMinInfo.amount());
            newReceipt.setIssuedDate(receiptMinInfo.issuedDate());
            newReceipt.setPaymentDate(receiptMinInfo.paymentDate());
            newReceipt.setExpiryDate(receiptMinInfo.expiryDate());
            newReceipt.setStatus(receiptMinInfo.status());
            return newReceipt;
        }

        return receiptJpaRepository.findById(receiptMinInfo.id()).orElseThrow(() -> new ReceiptNotFoundException(receiptMinInfo.id()));
    }

    default UserJpaEntity mapUserMinInfoToUserJpaEntity(UserMinInfo userMinInfo,
                                                        @Context UserJpaRepository userJpaRepository) {
        return userJpaRepository.findById(userMinInfo.id()).orElseThrow(() -> new UserNotFoundException(userMinInfo.id()));
    }
}
