package Accounts;

public interface Recompense {

    /**
     * Recompense some amount of money to the bank and reduce the value of loan recorded in this account.
     * Must not be greater than the current credit.
     * @param amount Amount of money to be recompensed.
     * @return Flag if compensation was successful.
     */
    public boolean recompense(double amount);
}
