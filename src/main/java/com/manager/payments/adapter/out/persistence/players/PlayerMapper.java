package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.in.rest.dto.PlayerDto;
import com.manager.payments.adapter.out.persistence.payments.PaymentJpaRepository;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaRepository;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.model.players.Player;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class, ReceiptMapper.class})
public interface PlayerMapper {

    Player toPlayer(PlayerJpaEntity playerJpaEntity);

    PlayerJpaEntity toPlayerJpaEntity(Player player, @Context PaymentJpaRepository paymentJpaRepository,
                                      @Context ReceiptJpaRepository receiptJpaRepository);

    PlayerDto toPlayerDto(Player player);

    @AfterMapping
    default void linkReceipts(@MappingTarget PlayerJpaEntity playerJpaEntity) {
        if (playerJpaEntity.getReceipts() != null) {
            playerJpaEntity.getReceipts().forEach(r -> r.setPlayer(playerJpaEntity));
        }
    }
}
