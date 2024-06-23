package net.ezzakri.ebankingbackend.repositories;

import net.ezzakri.ebankingbackend.entities.BankAccount;
import net.ezzakri.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findBankAccountByCustomerId(Long customerId);
}
