package org.sid.digitalbankingbackend.repositories;

import org.sid.digitalbankingbackend.entities.BankAccount;
import org.sid.digitalbankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}
