package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.entity.Account;
import com.backbase.example.accountservice.repository.AccountRepository;
import com.backbase.example.accountservice.security.JwtFilter;
import com.backbase.example.accountservice.security.JwtUtil;
import com.backbase.example.accountservice.security.SecurityConfig;
import com.backbase.example.accountservice.service.AccountService;
import com.backbase.example.accountservice.service.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Disabled
@ActiveProfiles("test")
@SpringBootTest(classes = {SecurityConfig.class, TokenBlacklistService.class, JwtUtil.class, AccountController.class, JwtFilter.class})
@Import(SecurityConfig.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testGetAllAccountsWithPagination() throws Exception {
        Account account1 = new Account(1L, "Account1", "123456789");
        Account account2 = new Account(2L, "Account2", "987654321");
        Pageable pageable = PageRequest.of(0, 2);
        Page<Account> accountPage = new PageImpl<>(Arrays.asList(account1, account2), pageable, 2);

        when(accountService.getAllAccounts(anyInt(), anyInt())).thenReturn(accountPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts")
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Account1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Account2"));
    }

    @Test
    public void testGetAccountByNumber() throws Exception {
        when(accountService.getAccountByAccountNumber(anyString())).thenReturn(Optional.of(new Account(1L, "Account1", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/account/123456789"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Account1"));
    }

    @Test
    public void testGetAccountByName() throws Exception {
        when(accountService.getAccountByAccountNumber(anyString())).thenReturn(Optional.of(new Account(1L, "Account1", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/account/Account1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Account1"));
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account(1L, "Account1", "123456789");
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Account1"));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        Account account = new Account(1L, "UpdatedAccount", "123456789");
        when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UpdatedAccount"));
    }
}
