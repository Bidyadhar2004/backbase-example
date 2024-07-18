package com.backbase.example.accountservice.service;

import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Page<Account> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findAll(pageable);
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> getAccountByAccountName(String accountName) {
        return accountRepository.findByAccountName(accountName);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id).map(existingAccount -> {
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountName(updatedAccount.getAccountName());
            return accountRepository.save(existingAccount);
        });
    }
}
