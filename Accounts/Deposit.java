package Accounts;

public interface Deposit {


    /**
     * Deposit an amount of money to some given account.
     * @param amount Amount to be deposited.
     * @return Flag if transaction is successful or not.
     */
    public boolean cashDeposit(double amount);

}
