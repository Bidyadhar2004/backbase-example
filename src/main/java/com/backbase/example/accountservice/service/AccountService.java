package com.backbase.example.accountservice.service;

import com.backbase.example.accountservice.dto.AccountDto;
import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Page<Account> getAllAccounts(Pageable pageable) {
        logger.info("Getting All Account details using pagination");
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts;
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        logger.info("Fetching account by account number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> getAccountByAccountName(String accountName) {
        logger.info("Fetching account by account name: {}", accountName);
        return accountRepository.findByAccountName(accountName);
    }

    public Account createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountName(accountDto.getAccountName());
        account.setAccountNumber(accountDto.getAccountNumber());
        logger.info("Account is created");
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long id, AccountDto accountDto) {
        // Check if the account exists
        if (accountRepository.existsById(id)) {
            Account account = new Account();
            account.setId(id);
            account.setAccountName(accountDto.getAccountName());
            account.setAccountNumber(accountDto.getAccountNumber());
            // Save the updated account
            Account updatedAccount = accountRepository.save(account);
            return Optional.of(updatedAccount);
        } else {
            // Return Optional.empty() if the account does not exist
            return Optional.empty();
        }
    }
}
