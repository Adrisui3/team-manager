package com.manager.payments.model.movements;

import lombok.Builder;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
public record Movement(
        LocalDate date,
        LocalDate valueDate,
        String movementName,
        String concept,
        BigDecimal amount
) {

    public static Movement parse(CSVRecord record) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return Movement.builder()
                .date(LocalDate.parse(record.get("Fecha"), formatter))
                .valueDate(LocalDate.parse(record.get("Fecha valor"), formatter))
                .movementName(record.get("Movimiento"))
                .concept(record.get("Más datos"))
                .amount(new BigDecimal(record.get("Importe").trim()
                        .replace(".", "")
                        .replace(",", ".")))
                .build();
    }

}
