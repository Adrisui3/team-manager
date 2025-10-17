package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptController(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ResponseDto<ReceiptDto>> getReceipt(@PathVariable("receiptId") UUID receiptId) {
        Receipt receipt =
                receiptRepository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), receiptMapper.toReceiptDto(receipt)));
    }

    @PutMapping("/{receiptId}/update-status/{newStatus}")
    public ResponseEntity<ResponseDto<ReceiptDto>> updatePaymentStatus(@PathVariable UUID receiptId,
                                                                       @PathVariable ReceiptStatus newStatus) {
        Receipt updatedReceipt = receiptRepository.updateStatus(receiptId, newStatus);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), receiptMapper.toReceiptDto(updatedReceipt)));
    }
}
