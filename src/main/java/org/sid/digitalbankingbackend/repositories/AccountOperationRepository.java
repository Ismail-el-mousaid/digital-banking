package org.sid.digitalbankingbackend.repositories;

import org.sid.digitalbankingbackend.entities.AccountOperation;
import org.sid.digitalbankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}
