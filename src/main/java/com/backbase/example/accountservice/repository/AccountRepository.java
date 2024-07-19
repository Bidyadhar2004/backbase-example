package com.backbase.example.accountservice.repository;

import com.backbase.example.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Page<Account> findAll(Pageable pageable);
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByAccountName(String accountName);
}
