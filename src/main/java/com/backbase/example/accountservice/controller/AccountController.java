package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.dto.AccountDto;
import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This AccountController is handling for account operations for a user
 */
@RestController
@RequestMapping("/api")
@Tag(name = "AccountController", description = "REST APIs related to Account Service")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * This method is used for get all accounts for a user
     * @param page
     * @param size
     * @return Page<Account> which contains account details with requested pagination
     */
    @Operation(summary = "Get all account details")
    @GetMapping("/accounts")
    public Page<Account> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Get all accounts of a user");
        return accountService.getAllAccounts(page, size);
    }

    /**
     * This method is used for get specific account details for a user
     * @param accountNumber
     * @param accountName
     * @return Optional<Account> which contains single account details
     */
    @Operation(summary = "Get specific account details")
    @GetMapping("/account")
    public Optional<Account> getAccount(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName) {
        if (accountNumber != null) {
            return accountService.getAccountByAccountNumber(accountNumber);
        } else if (accountName != null) {
            return accountService.getAccountByAccountName(accountName);
        }
        logger.info("Getting account details based on accountNumber or accountName");
        return Optional.empty();
    }


    /**
     * This method is used for create a new Account details for a user
     * @param accountDto
     * @return Account which contains a newly created Account details for a user
     */
    @Operation(summary = "Create Account")
    @PostMapping("/accounts")
    public Account createAccount(@RequestBody AccountDto accountDto) {
        logger.info("Account is created");
        return accountService.createAccount(accountDto);
    }

    /**
     * This method is used for update an existing Account details for a user
     * @param id
     * @param accountDto
     * @return Optional<Account> which contains an updated Account details for a user
     */
    @Operation(summary = "Update Account")
    @PutMapping("/accounts/{id}")
    public Optional<Account> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        logger.info("Account is updated for {}", id);
        return accountService.updateAccount(id, accountDto);
    }
}
