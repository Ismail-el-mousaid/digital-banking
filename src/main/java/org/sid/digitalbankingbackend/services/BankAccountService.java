package org.sid.digitalbankingbackend.services;

import org.sid.digitalbankingbackend.entities.BankAccount;
import org.sid.digitalbankingbackend.entities.CurrentAccount;
import org.sid.digitalbankingbackend.entities.Customer;
import org.sid.digitalbankingbackend.entities.SavingAccount;
import org.sid.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.digitalbankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double montant, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double montant, String description) throws BankAccountNotFoundException;
    void transferer(String accountIdSource, String accountIdDestination, double montant) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();
}
