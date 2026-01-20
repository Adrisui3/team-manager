package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentJpaEntity;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "player")
public class PlayerJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String personalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerGender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerPaymentAssignmentJpaEntity> assignments = new ArrayList<>();
}
