package org.sid.digitalbankingbackend.web;

import lombok.AllArgsConstructor;
import org.sid.digitalbankingbackend.dtos.AccountHistoryDTO;
import org.sid.digitalbankingbackend.dtos.AccountOperationDTO;
import org.sid.digitalbankingbackend.dtos.BankAccountDTO;
import org.sid.digitalbankingbackend.entities.BankAccount;
import org.sid.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.digitalbankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> listBankAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/bankAccounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/bankAccounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }
}
