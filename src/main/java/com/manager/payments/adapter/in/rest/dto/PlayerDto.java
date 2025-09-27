package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PlayerDto(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                        Category category, PlayerStatus status, List<PaymentMinInfo> payments,
                        List<ReceiptMinInfoDto> receipts) {
}
