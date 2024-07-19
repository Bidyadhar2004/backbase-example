package com.backbase.example.accountservice.service;

import com.backbase.example.accountservice.dto.AccountDto;
import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccountsWithPagination() {
        Account account1 = new Account(1L, "Account1", "123456789");
        Account account2 = new Account(2L, "Account2", "987654321");

        List<Account> accounts = Arrays.asList(account1, account2);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());

        when(accountRepository.findAll(pageable)).thenReturn(accountPage);

        Page<Account> result = accountService.getAllAccounts(pageable);

        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(1, result.getContent().get(0).getId());
        Assertions.assertEquals(2, result.getContent().get(1).getId());
    }

    @Test
    public void testGetAccountByNumber() {
        Account account = new Account(1L, "Account1", "123456789");
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountByAccountNumber("123456789");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("123456789", result.get().getAccountNumber());
    }

    @Test
    public void testGetAccountByName() {
        Account account = new Account(1L, "Account1", "123456789");
        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountByAccountName("Account1");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Account1", result.get().getAccountName());
    }

    @Test
    public void testCreateAccount() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setAccountName("Account1");
        accountDto.setAccountNumber("123456789");

        Account account = new Account(accountDto.getId(), accountDto.getAccountName(), accountDto.getAccountNumber());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.createAccount(accountDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Account1", result.getAccountName());
    }

    @Test
    public void testUpdateAccount() {
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountName("Updated Account");
        accountDto.setAccountNumber("345123456");
        Account updatedAccount = new Account(accountId, accountDto.getAccountName(), accountDto.getAccountNumber());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(updatedAccount));
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        Optional<Account> result = accountService.updateAccount(1L, accountDto);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Updated Account", result.get().getAccountName());
    }
}
