package net.ezzakri.ebankingbackend.exceptions;

public class BankAccountNotFound extends Exception {
    public BankAccountNotFound(String bankAccountNotFound) {
        super(bankAccountNotFound);
    }
}
