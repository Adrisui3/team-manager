package com.manager.payments.application.service.receipts;

import com.manager.payments.application.port.in.receipts.ParseMovementsUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.EmptyFileException;
import com.manager.payments.model.exceptions.InvalidFileFormatException;
import com.manager.payments.model.exceptions.ParsingException;
import com.manager.payments.model.movements.Movement;
import com.manager.payments.model.movements.ReceiptMatch;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseMovementsService implements ParseMovementsUseCase {

    private final ReceiptRepository repository;

    @Override
    public List<ReceiptMatch> parseMovements(MultipartFile file) {
        validateCsv(file);
        List<Movement> movements = buildAllMovements(file);

        List<ReceiptMatch> matches = new ArrayList<>();
        for (Movement movement : movements) {
            Optional<Receipt> optionalReceipt = repository.findUnpaidByCode(movement.concept());
            optionalReceipt.ifPresent(receipt -> {
                ReceiptMatch match = ReceiptMatch.build(receipt, movement);
                if (match.amountCorrect()) {
                    repository.save(receipt.toBuilder()
                            .status(ReceiptStatus.PAID)
                            .paymentDate(movement.valueDate())
                            .build());
                }

                matches.add(match);
            });
        }

        log.info("Found {} matches out of {} bank movements", matches.size(), movements.size());
        return List.copyOf(matches);
    }

    private List<Movement> buildAllMovements(MultipartFile file) {
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .get();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser parser = CSVParser.parse(reader, format)) {
            return parser.getRecords().stream().map(Movement::parse).toList();
        } catch (IOException e) {
            throw new ParsingException(e.getMessage());
        }
    }

    private void validateCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".csv")) {
            throw new InvalidFileFormatException();
        }
    }
}
