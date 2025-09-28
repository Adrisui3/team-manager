package com.manager.payments.model.billing;

import java.time.LocalDate;

public record BillingPeriod(LocalDate start, LocalDate end) {
}
