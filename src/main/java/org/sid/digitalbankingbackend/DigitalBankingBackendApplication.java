package org.sid.digitalbankingbackend;

import org.sid.digitalbankingbackend.dtos.BankAccountDTO;
import org.sid.digitalbankingbackend.dtos.CurrentBankAccountDTO;
import org.sid.digitalbankingbackend.dtos.CustomerDTO;
import org.sid.digitalbankingbackend.dtos.SavingBankAccountDTO;
import org.sid.digitalbankingbackend.entities.*;
import org.sid.digitalbankingbackend.enums.AccountStatus;
import org.sid.digitalbankingbackend.enums.OperationType;
import org.sid.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.digitalbankingbackend.exceptions.CustomerNotFoundException;
import org.sid.digitalbankingbackend.repositories.AccountOperationRepository;
import org.sid.digitalbankingbackend.repositories.BankAccountRepository;
import org.sid.digitalbankingbackend.repositories.CustomerRepository;
import org.sid.digitalbankingbackend.services.BankAccountService;
import org.sid.digitalbankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackendApplication.class, args);
    }

  //  @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Ismail", "Mohamed", "Marouane").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccountDTO : bankAccounts){
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccountDTO instanceof SavingBankAccountDTO)
                        accountId = ((SavingBankAccountDTO) bankAccountDTO).getId();
                    else
                        accountId = ((CurrentBankAccountDTO) bankAccountDTO).getId();
                    bankAccountService.credit(accountId, 10000+Math.random()*12000, "Credit");
                    bankAccountService.debit(accountId, 1000+Math.random()*9000, "Debit");
                }
            }
        };
    }


  //  @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Ismail", "Mohamed", "Samir").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(account -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*10000);
                    accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(account);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

   // @Bean
    CommandLineRunner consulterDonnees(BankService bankService){
        return args -> {
            bankService.consulter();

        };
    }

}
