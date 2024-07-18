package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public Page<Account> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return accountService.getAllAccounts(page, size);
    }

    @GetMapping("/account")
    public Optional<Account> getAccount(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName) {
        if (accountNumber != null) {
            return accountService.getAccountByAccountNumber(accountNumber);
        } else if (accountName != null) {
            return accountService.getAccountByAccountName(accountName);
        }
        return Optional.empty();
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/accounts/{id}")
    public Optional<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }
}
