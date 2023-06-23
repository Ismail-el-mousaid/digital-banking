package org.sid.digitalbankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.digitalbankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
//Manipuler l'héritage en BD (SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING) //champ dans BD avec lequele on distingue entre les 2 classes fils
@Data @AllArgsConstructor @NoArgsConstructor
public abstract class BankAccount {
    @Id @Column(length = 100)
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING) //stocker enum en BD en format String (pas 0,1,2)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
 //   @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER) //Charger les opérations de chaque compte en mémoire
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)    //chargement à la demande
    private List<AccountOperation> accountOperations;

}
