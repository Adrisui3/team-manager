package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.payments.Periodicity;

import java.time.LocalDate;

public record CreatePaymentRequestDTO(double amount, String code, String name, String description, LocalDate startDate,
                                      LocalDate endDate, Periodicity periodicity) {
}
