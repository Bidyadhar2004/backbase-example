package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.dto.AccountDto;
import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.repository.AccountRepository;
import com.backbase.example.accountservice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountController accountController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllAccountsWithPagination() throws Exception {
        Account account1 = new Account(1L, "Account1", "123456789");
        Account account2 = new Account(2L, "Account2", "987654321");

        List<Account> accounts = Arrays.asList(account1, account2);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Page<Account> accountPage = new PageImpl<>(accounts, pageable, accounts.size());

        Mockito.when(accountService.getAllAccounts(pageable)).thenReturn(accountPage);

        Mockito.when(accountRepository.findAll(pageable)).thenReturn(accountPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].accountName").value("Account1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].accountName").value("Account2"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAccountByNumber() throws Exception {
        String accountNumber = "123456789";
        Account account = new Account(1L, "Test Account", accountNumber);

        when(accountService.getAccountByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/account")
                .param("accountNumber", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName").value("Test Account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(accountNumber));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAccountByName() throws Exception {
        String accountName = "Test Account";
        Account account = new Account(1L, accountName, "123456789");

        when(accountService.getAccountByAccountName(accountName)).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api//account")
                .param("accountName", accountName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName").value(accountName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateAccount() throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountName("New Account");
        accountDto.setAccountNumber("123456789");
        Account createdAccount = new Account(1L, accountDto.getAccountName(), accountDto.getAccountNumber());

        when(accountService.createAccount(accountDto)).thenReturn(createdAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName").value("New Account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUpdateAccount() throws Exception {
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountName("Updated Account");
        accountDto.setAccountNumber("345123456");
        Account updatedAccount = new Account(accountId, accountDto.getAccountName(), accountDto.getAccountNumber());

        when(accountService.updateAccount(accountId, accountDto)).thenReturn(Optional.of(updatedAccount));


        mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName").value("Updated Account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("345123456"));
    }
}
