package com.manager.email.adapter.out.persistence;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmailOutboxJpaRepositoryAdapter implements EmailRepository {

    private final EmailOutboxJpaRepository repository;
    private final EmailMapper mapper;

    @Override
    public void save(Email email) {
        EmailOutboxJpaEntity entity = mapper.toEntity(email);
        repository.save(entity);
    }

    @Override
    public int deleteExpired(LocalDateTime date) {
        return repository.deleteExpired(date);
    }

    @Override
    public List<Email> findAllToBeSent(LocalDateTime targetDate) {
        List<EmailOutboxJpaEntity> emails = repository.findAllToBeSent(targetDate);
        return emails.stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Email> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
