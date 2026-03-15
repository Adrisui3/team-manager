package com.manager.payments.application.port.in.receipts;

import com.manager.payments.model.movements.ReceiptMatch;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ParseMovementsUseCase {

    List<ReceiptMatch> parseMovements(MultipartFile file);

}
