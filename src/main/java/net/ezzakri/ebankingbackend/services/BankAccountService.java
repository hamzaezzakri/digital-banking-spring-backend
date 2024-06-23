package net.ezzakri.ebankingbackend.services;

import net.ezzakri.ebankingbackend.dtos.*;
import net.ezzakri.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.ezzakri.ebankingbackend.exceptions.BankAccountNotFound;
import net.ezzakri.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFound;
    void debit(String accountId, double amount, String description) throws BankAccountNotFound, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFound;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFound, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFound;

    List<CustomerDTO> searchCustomers(String keyword);
    List<BankAccountDTO> findBankAccountByCustomerId(Long customerId);
}
