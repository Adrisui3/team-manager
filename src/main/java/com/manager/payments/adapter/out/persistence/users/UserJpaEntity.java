package com.manager.payments.adapter.out.persistence.users;

import com.manager.payments.adapter.out.persistence.payments.PaymentJpaEntity;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptJpaEntity;
import com.manager.payments.model.users.Category;
import com.manager.payments.model.users.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String personalId;

    private String name;
    private String surname;

    private String email;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Category category = Category.NONE;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ENABLED;

    @ManyToMany
    @JoinTable(
            name = "user_payment",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_id")
    )
    private List<PaymentJpaEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReceiptJpaEntity> receiptJpaEntities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<ReceiptJpaEntity> getReceipts() {
        return receiptJpaEntities;
    }

    public void setReceipts(List<ReceiptJpaEntity> receiptJpaEntities) {
        this.receiptJpaEntities = receiptJpaEntities;
    }

    public List<PaymentJpaEntity> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentJpaEntity> paymentJpaEntities) {
        this.payments = paymentJpaEntities;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
