package com.backbase.example.accountservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDto {

    private String accountName;
    private String accountNumber;
}
