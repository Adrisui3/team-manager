package com.manager.payments.adapter.out.persistence.receipts;

import com.manager.payments.adapter.in.rest.dto.ReceiptMinInfoDto;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaEntity;
import com.manager.payments.adapter.out.persistence.players.PlayerJpaRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.players.PlayerMinInfo;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    Receipt toReceipt(ReceiptJpaEntity receiptJpaEntity);

    ReceiptMinInfoDto toReceiptMinInfoDto(ReceiptMinInfo receipt);

    ReceiptMinInfoDto toReceiptMinInfoDto(Receipt receipt);

    List<ReceiptMinInfoDto> toReceiptMinInfoDto(List<ReceiptMinInfo> receipt);

    ReceiptJpaEntity toReceiptJpaEntity(Receipt receipt, @Context PlayerJpaRepository playerJpaRepository);

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

    default PlayerJpaEntity mapPlayerMinInfoToUserJpaEntity(PlayerMinInfo playerMinInfo,
                                                            @Context PlayerJpaRepository playerJpaRepository) {
        return playerJpaRepository.findById(playerMinInfo.id()).orElseThrow(() -> new PlayerNotFoundException(playerMinInfo.id()));
    }
}
