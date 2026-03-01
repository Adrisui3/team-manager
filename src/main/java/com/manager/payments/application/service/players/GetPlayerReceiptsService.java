package com.manager.payments.application.service.players;

import com.manager.payments.application.port.in.players.GetPlayerReceiptsUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GetPlayerReceiptsService implements GetPlayerReceiptsUseCase {

    private final PlayerRepository playerRepository;
    private final ReceiptRepository receiptRepository;

    @Override
    public Page<Receipt> getPlayerReceipts(UUID playerId, ReceiptStatus status, Pageable pageable) {
        if (!playerRepository.existsById(playerId)) {
            throw PlayerNotFoundException.byId(playerId);
        }

        return status == null ? receiptRepository.findAllByPlayerId(playerId, pageable) :
                receiptRepository.findAllByPlayerIdAndStatus(playerId, pageable, status);
    }
}
