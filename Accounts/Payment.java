package Accounts;

public interface Payment {

    /**
     * Pay a certain amount of money into a given account object.
     * This is different from Fund Transfer as paying does not have any sort of
     * processing fee.
     * @param account Target account to pay money into.
     * @param amount
     * @throws IllegalAccountType Payment can only be processed between legal account types.
     */
    public boolean pay(Account account, double amount) throws IllegalAccountType;

}
