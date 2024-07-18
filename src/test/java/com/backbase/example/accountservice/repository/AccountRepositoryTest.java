package com.backbase.example.accountservice.repository;

import com.backbase.example.accountservice.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account(1L, "Test Account", "123456789");
        accountRepository.save(account);
    }

    @Test
    public void testFindByName() {
        Optional<Account> foundAccount = accountRepository.findByAccountName(account.getAccountName());
        Assertions.assertTrue(foundAccount.isPresent());
        Assertions.assertEquals("Test Account", foundAccount.get().getAccountName());
    }

    @Test
    public void testSave() {
        Account newAccount = new Account(3L, "New Account", "987654321");
        Account savedAccount = accountRepository.save(newAccount);

        Assertions.assertNotNull(savedAccount);
        Assertions.assertEquals("New Account", savedAccount.getAccountName());
        Assertions.assertEquals("987654321", savedAccount.getAccountNumber());
    }

    @Test
    public void testFindAll() {
        Account anotherAccount = new Account(4L, "111111111",  "Ravi");

        accountRepository.save(anotherAccount);

        Iterable<Account> accounts = accountRepository.findAll();
        int count = 0;
        for (Account acc : accounts) {
            count++;
        }
        Assertions.assertEquals(2, count);
    }
}
