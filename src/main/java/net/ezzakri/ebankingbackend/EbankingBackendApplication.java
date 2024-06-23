package net.ezzakri.ebankingbackend;

import net.ezzakri.ebankingbackend.dtos.BankAccountDTO;
import net.ezzakri.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.ezzakri.ebankingbackend.dtos.CustomerDTO;
import net.ezzakri.ebankingbackend.dtos.SavingBankAccountDTO;
import net.ezzakri.ebankingbackend.entities.*;
import net.ezzakri.ebankingbackend.enums.AccountStatus;
import net.ezzakri.ebankingbackend.enums.OperationType;
import net.ezzakri.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.ezzakri.ebankingbackend.exceptions.BankAccountNotFound;
import net.ezzakri.ebankingbackend.exceptions.CustomerNotFoundException;
import net.ezzakri.ebankingbackend.repositories.AccountOperationRepository;
import net.ezzakri.ebankingbackend.repositories.BankAccountRepository;
import net.ezzakri.ebankingbackend.repositories.CustomerRepository;
import net.ezzakri.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Hassan", "Imane", "Mohamed").forEach(name->{
				CustomerDTO customerDTO =  new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customerDTO);
			});

			bankAccountService.listCustomers().forEach(customer -> {

                try {
					bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
            });

			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();

			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAccountDTO){
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					}
					else{
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
					bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
				}
			}
		};
	}

	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(customer->{
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

			bankAccountRepository.findAll().stream().forEach(acc->{
				for (int i = 0; i < 5; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}
}
