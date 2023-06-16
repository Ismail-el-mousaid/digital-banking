package org.sid.digitalbankingbackend.services;

import org.sid.digitalbankingbackend.entities.*;
import org.sid.digitalbankingbackend.enums.OperationType;
import org.sid.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.digitalbankingbackend.exceptions.CustomerNotFoundException;
import org.sid.digitalbankingbackend.repositories.AccountOperationRepository;
import org.sid.digitalbankingbackend.repositories.BankAccountRepository;
import org.sid.digitalbankingbackend.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService{

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new Customer");
        Customer saveCustomer = customerRepository.save(customer);
        return saveCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount saveCurrentAccount = bankAccountRepository.save(currentAccount);
        return saveCurrentAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount saveSavingAccount = bankAccountRepository.save(savingAccount);
        return saveSavingAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double montant, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance()<montant)
            throw new BalanceNotSufficientException("Balance Not Sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(montant);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-montant);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double montant, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(montant);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+montant);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transferer(String accountIdSource, String accountIdDestination, double montant) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, montant, "Transfer to "+accountIdDestination);
        credit(accountIdDestination, montant, "Transfer from "+accountIdSource);

    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }

}
