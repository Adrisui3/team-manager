package com.manager.payments.adapter.in.rest.dto;

import java.time.LocalDate;

public record CreatePaymentRequestDTO(double amount, String name, String description, LocalDate startDate,
                                      LocalDate endDate, int periodDays) {
}
