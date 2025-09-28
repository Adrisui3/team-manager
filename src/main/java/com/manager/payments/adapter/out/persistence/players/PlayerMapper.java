package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.in.rest.dto.models.PlayerDto;
import com.manager.payments.model.players.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player toPlayer(PlayerJpaEntity playerJpaEntity);

    PlayerJpaEntity toPlayerJpaEntity(Player player);

    PlayerDto toPlayerDto(Player player);

}
