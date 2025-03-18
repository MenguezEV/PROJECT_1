package Accounts;

public interface Withdrawal {
    /**
     * Withdraws an amount of money using a given medium.
     * @param amount Amount of money to be withdrawn from.
     */
    public boolean withdrawal(double amount);
}
