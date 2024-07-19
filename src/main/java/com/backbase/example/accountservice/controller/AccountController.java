package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.dto.AccountDto;
import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Account> accountPage = accountService.getAllAccounts(pageable);
        logger.info("Page: {}, Size: {}, Total Elements: {}", page, size, accountPage.getTotalElements());

        return accountPage;
    }

    /**
     * This method is used for get specific account details for a user
     * @param accountNumber
     * @param accountName
     * @return Optional<Account> which contains single account details
     */
    @Operation(summary = "Get specific account details")
    @GetMapping("/account")
    public ResponseEntity<?> getAccount(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName) {
        logger.info("Received request to get account with accountNumber: {} and accountName: {}", accountNumber, accountName);

        logger.info("Received request to get account with accountNumber: {} and accountName: {}", accountNumber, accountName);

        Optional<Account> account;
        if (accountNumber != null) {
            account = accountService.getAccountByAccountNumber(accountNumber);
        } else if (accountName != null) {
            account = accountService.getAccountByAccountName(accountName);
        } else {
            logger.info("No accountNumber or accountName provided, returning bad request.");
            return ResponseEntity.badRequest().body("Please provide accountNumber or accountName.");
        }

        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        logger.info("Updating account with ID: {}", id);

        Optional<Account> updatedAccount = accountService.updateAccount(id, accountDto);

        if (updatedAccount.isPresent()) {
            // Return the updated account with HTTP 200 OK status
            return ResponseEntity.ok(updatedAccount.get());
        } else {
            // Return HTTP 404 Not Found if the account was not found
            return ResponseEntity.notFound().build();
        }
    }
}
