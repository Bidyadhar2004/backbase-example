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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Page<Account> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        logger.info("Getting All Account details using page {} and size {}", page, size);
        logger.info("Getting All accounts of a user from repository {}", accountRepository.findAll(pageable));
        return accountRepository.findAll(pageable);
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        logger.info("Getting Account details using accountNumber {}", accountNumber);
        logger.info("Getting Account details using accountNumber from repository {}", accountRepository.findByAccountNumber(accountNumber));
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> getAccountByAccountName(String accountName) {
        logger.info("Getting Account details using accountName {}", accountName);
        logger.info("Getting Account details using accountName from repository {}", accountRepository.findByAccountName(accountName));
        return accountRepository.findByAccountName(accountName);
    }

    public Account createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountName(account.getAccountName());
        logger.info("Account is created");
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long id, AccountDto updatedAccount) {
        logger.info("Account is updated");
        return accountRepository.findById(id).map(existingAccount -> {
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountName(updatedAccount.getAccountName());
            return accountRepository.save(existingAccount);
        });
    }
}
