# Team Manager Payments Service

Team Manager helps grassroots and semi-professional clubs stay on top of member dues. This Spring Boot service powers the payment operations so treasurers always know who owes what and which receipts are outstanding.

> **Current status:** the application focuses solely on back-office payment administration. Other club-management functionality is not included here.

## Payment capabilities available today

- **Create payment programs** for recurring dues or seasonal registrations, including amount, descriptive copy, validity window, and billing cadence.
- **Register players and link them to payment programs** so invoices are created automatically without double data entry.
- **Generate receipts for each billing cycle** with automatic pro-rating when a player joins mid-cycle and status tracking inside the player record.
- **Monitor overdue and expiring payments** via nightly jobs that update receipt statuses and highlight who needs follow-up.
- **Update payment records manually when needed** with endpoints to fetch, assign, or remove payments and to confirm settlement of offline transactions.

## Architecture at a glance

- **Tech stack:** Java 21, Spring Boot 3, Spring Data JPA, Spring Security with JWT, and PostgreSQL as the system of record.
- **Hexagonal-inspired design:** domain models live in `com.manager.payments.model`, application services expose use cases, and REST adapters drive the API layer.
- **Scheduled billing engine:** cron-triggered jobs run once per day to advance payment cycles and mark overdue items.
- **Secure by default:** only the authentication endpoints are public. Everything else requires a JWT issued during login.

## Current limitations

- Only administrator-driven back-office flows are available; there is no player self-service portal.
- Payments are stored as commitments and receipts without integration to payment gateways, reconciliation tooling, or accounting exports.
- Email delivery depends on SMTP credentials provided by the club, and transactional templates remain intentionally minimal.
