package net.ezzakri.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String balanceNorSufficient) {
        super(balanceNorSufficient);
    }
}
