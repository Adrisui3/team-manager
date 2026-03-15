package com.manager.payments.model.movements;

import com.manager.payments.model.exceptions.ParsingException;
import lombok.Builder;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Builder
public record Movement(
        LocalDate date,
        LocalDate valueDate,
        String movementName,
        String concept,
        BigDecimal amount
) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static Movement parse(CSVRecord record) {
        try {
            return Movement.builder()
                    .date(LocalDate.parse(record.get("Fecha"), FORMATTER))
                    .valueDate(LocalDate.parse(record.get("Fecha valor"), FORMATTER))
                    .movementName(record.get("Movimiento"))
                    .concept(record.get("Más datos"))
                    .amount(new BigDecimal(record.get("Importe").trim()
                            .replace(".", "")
                            .replace(",", ".")))
                    .build();
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new ParsingException("Invalid data on row " + record.getRecordNumber() + ": " + e.getMessage());
        }
    }

}
