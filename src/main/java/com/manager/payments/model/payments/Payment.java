package com.manager.payments.model.payments;

import com.manager.payments.model.exceptions.CannotActivatePaymentException;
import com.manager.payments.model.exceptions.ManuallyExpirePaymentException;
import com.manager.payments.model.exceptions.UpdateStatusExpiredPaymentException;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Payment(UUID id, String code, BigDecimal amount, String name, String description, LocalDate startDate,
                      LocalDate endDate, Periodicity periodicity, PaymentStatus status, LocalDateTime updatedAt,
                      LocalDateTime createdAt) {

    public Payment update(String newName, String newDescription, PaymentStatus newStatus, LocalDate currentDate) {
        if (status() == PaymentStatus.EXPIRED && status() != newStatus)
            throw new UpdateStatusExpiredPaymentException();

        if (newStatus == PaymentStatus.EXPIRED && status() != PaymentStatus.EXPIRED)
            throw new ManuallyExpirePaymentException();

        if (status() == PaymentStatus.INACTIVE && newStatus == PaymentStatus.ACTIVE && (periodicity() != Periodicity.ONCE) && (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)))
            throw new CannotActivatePaymentException();

        return toBuilder()
                .name(newName)
                .description(newDescription)
                .status(newStatus)
                .build();
    }
}
