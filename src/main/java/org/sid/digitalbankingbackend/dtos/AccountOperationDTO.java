package org.sid.digitalbankingbackend.dtos;

import lombok.Data;
import org.sid.digitalbankingbackend.entities.BankAccount;
import org.sid.digitalbankingbackend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}

