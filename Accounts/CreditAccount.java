package Accounts;

import Bank.Bank;

public class CreditAccount extends Account implements Payment, Recompense{
    private double loan;

    public CreditAccount(Bank bank, String accountNumber, String ownerFname, String ownerLname, String ownerGmail, String pin) {
        super(bank, accountNumber, ownerFname, ownerLname, ownerGmail, pin);
        this.loan = 0;
    }

    /**
     * Returns a statement describing the current loan amount in the account.
     * @return A string representation of the current loan amount.
     */
    public String getLoanStatement() {
        return "Current Loan Amount: " + loan;
    }

    /**
     * Determines if the specified amount adjustment can be credited to the account without exceeding the credit limit
     * or resulting in a negative balance.
     *
     * @param amountAdjustment The amount to adjust the loan by. Can be positive or negative.
     * @return {@code true} if the adjustment can be credited, {@code false} otherwise.
     */
    private boolean canCredit(double amountAdjustment) {
        double adjustedAmount = amountAdjustment + loan;
        if (adjustedAmount > getBank().getCreditLimit() || adjustedAmount < 0) {
            return false;
        }
        return true;
    }

    /**
     * Adjusts the loan amount by a specified value. The adjustment is applied
     * only if it satisfies the credit constraints defined in the canCredit method.
     *
     * @param amountAdjustment The amount by which the loan will be adjusted.
     *                          Can be positive (to increase the loan) or negative
     *                          (to decrease the loan). Must adhere to credit limit
     *                          and balance rules.
     */
    private void adjustLoanAmount(double amountAdjustment) {
        if (canCredit(amountAdjustment)) {
            loan += amountAdjustment;
        }
        else {
            System.out.println("The amount cannot be credited due to exceeding the credit limit or being negative.");
        }
    }


    /**
     * Processes a payment from the specified account to this CreditAccount.
     * Updates the loan amount and logs the transaction if the payment is successful.
     * @param account The account making the payment. Must be of type SavingsAccount.
     * @param amount The amount to be paid. Must be a valid positive double value.
     * @return {@code true} if the payment was successful, {@code false} otherwise.
     * @throws IllegalAccountType If the provided account is a CreditAccount.
     */
    @Override
    public boolean pay(Account account, double amount) throws IllegalAccountType {
        if (account instanceof CreditAccount) {
            throw new IllegalAccountType("Credit account cannot pay other credit accounts.");
        }

        SavingsAccount payer = (SavingsAccount) account;
        boolean payed = payer.withdrawal(amount);

        if (payed) {
            adjustLoanAmount(amount);

            String description = String.format("Paid $%f ",amount);
            addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Payment, description);

        }
        return payed;
    }

    /**
     * Recompenses a specified amount of money to the bank by reducing the loan amount in this account.
     * The recompense amount must not exceed the current loan amount.
     * @param amount The amount of money to be recompensed. Must be a positive double value not greater than the current loan.
     * @return {@code true} if the recompense is successful, {@code false} if the amount exceeds the current loan.
     */
    @Override
    public boolean recompense(double amount) {
        if (amount > loan) {
            System.out.println("The amount cannot be recompensed due to exceeding the loan amount.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("The amount cannot be recompensed due to being none or negative.");
            return false;
        }

        adjustLoanAmount(-amount);

        String description = String.format("Recompesed $%f", amount);
        addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Recompense, description);
        return true;

    }

    public String toString() {
        return super.toString() + "\n\t\t" + getLoanStatement();
    }
}
