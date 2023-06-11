package org.sid.digitalbankingbackend.services;

import org.sid.digitalbankingbackend.entities.BankAccount;
import org.sid.digitalbankingbackend.entities.CurrentAccount;
import org.sid.digitalbankingbackend.entities.SavingAccount;
import org.sid.digitalbankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("3e7c8cd1-089d-41e7-b70c-f663fc01f2e2").orElse(null);
        System.out.println("******************************");
        if (bankAccount != null) {
            System.out.println(bankAccount.getClass().getSimpleName());
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getCustomer().getName());
        }

        if (bankAccount instanceof CurrentAccount){
            System.out.println("overDraft : "+((CurrentAccount) bankAccount).getOverDraft());
        } else if (bankAccount instanceof SavingAccount){
            System.out.println("taux interet : "+((SavingAccount) bankAccount).getInterestRate());
        }
        //Parcourir les opérations du compte
        //Meme que la chargement est en Type LAZY, l'annotation Transactional est envoyé une requete vers BD pour charger les opérations
        bankAccount.getAccountOperations().forEach(op -> {
            System.out.println(op.getType() +"\t" +op.getOperationDate() +"\t" +op.getAmount());
        });
    }
}
