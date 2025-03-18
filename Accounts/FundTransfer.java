package Accounts;

import Bank.Bank;

public interface FundTransfer{
    /**
     * Cross-transferring of money from one account of one bank, and another account of another bank.
     * Each transfer may require an additional process fee, which is directly deducted
     * from the source account's account balance.
     * <br><br>
     * Cannot proceed if one of the following is true:
     * <ul>
     *     <li>Insufficient balance from source account.</li>
     *     <li>Account types are incompatible for fund transfer purposes.</li>
     * </ul>
     * @param bank Bank.Bank ID of the recepient's account.
     * @param account Recipient's account number.
     * @param amount Amount of money to be transferred.
     * @throws IllegalAccountType This error is thrown depending on the rules set upon. Generally
     * occurs when fund transferring from an incompatible account type.
     */
    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType;

    /**
     * Transfer money from one account on the same bank, using the
     * recepient's account number.
     * <br><br>
     * Cannot proceed if one of the following is true:
     * <ul>
     *     <li>Insufficient balance from source account.</li>
     *     <li>Recepient account does not exist.</li>
     *     <li>Recepient account is from another bank.</li>
     * </ul>
     * @param account Accounts.Account number of the recepient.
     * @param amount Amount of money to be transferred.
     * @throws IllegalAccountType This error is thrown depending on the rules set upon. Generally occurs
     * when fund transferring from an incompatible account type.
     */
    public boolean transfer(Account account, double amount) throws IllegalAccountType;
}