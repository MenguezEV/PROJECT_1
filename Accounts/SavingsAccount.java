package Accounts;

import Bank.Bank;

public class SavingsAccount extends Account implements FundTransfer,Deposit, Withdrawal {
    private double balance;

    public SavingsAccount(Bank bank, String AccountNumber,  String pin, String ownerFName, String ownerLName, String ownerEmail, double balance) {
        super(bank, AccountNumber, pin, ownerFName, ownerLName, ownerEmail);
        this.cashDeposit(balance);
    }

    /**
     * Gets the balance of a savings account
     * @return balance of this account
     */
    public double getAccountBalance() {
        return balance;
    }

    /**
     * Checks if the user have enough balance to a specific transaction
     * @param amount of money for a transaction
     * @return boolean if the account have enough balance
     */
    private boolean hasEnoughBalance(double amount) {
        return getAccountBalance() >= amount;
    }

    /**
     *  Gets the balance of a savings account
     * @return balance of this account in a String
     */
    public String getAccountBalanceStatement() {
        return String.valueOf(getAccountBalance());
    }

    /**
     * Warning if the user have insufficientBalance for a transaction
     */
    private void insufficientBalance() {
        System.out.printf("Current Balance: %s. Transaction failed due to insufficient funds.%n", getAccountBalanceStatement());
    }


    /**
     * Transfer of funds to other banks
     * -This checks if the account is in the argument bank
     * -Checks if the amount did not exceed the banks limits
     * -Also checks for the balance of this account if it has enough for this transaction
     * Adds this transaction to the transactions log
     * @param bank Bank.Bank ID of the recepient's account.
     * @param account Recipient's account number.
     * @param amount Amount of money to be transferred.
     * @return true if the transaction is successful, else false
     * @throws IllegalAccountType if the account is not an instance of SavingAccount
     */
    @Override
    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalAccountType("You cannot transfer to a non-savings account.");
        }

        //Making sure if the account given is in the given bank
        SavingsAccount recipient = (SavingsAccount) bank.getBankAccount(bank, account.getAccountNumber());

        if (recipient == null) {
            System.out.printf("The recipient account does not exist in %s.", bank.getName());
        }
        if (amount > getBank().getWithdrawLimit()) {
            System.out.println("The amount you are trying to transfer is too large for the bank.");
        }
        else if (!hasEnoughBalance(amount)){
            this.insufficientBalance();
        }
        else{
            recipient.adjustBalance(amount);
            this.adjustBalance(-(amount + getBank().getProcessingFee()));

            // Saving the transaction in this account's log
            String description = String.format("Transfer $%f from %s Account No.%s to %s Account No.%s", amount, getBank(),this.getAccountNumber(), bank.getName(), recipient.getAccountNumber());
            addNewTransaction(this.getAccountNumber(), Transaction.Transactions.FundTransfer, description);
            recipient.addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Deposit, description);

            return true;
        }
        return false;
    }

    /**
     * Transfer fund in the same bank
     * -checks if the given account exist in the same bank as this account
     * -checks the bank withdraw limits
     * -checks this accounts balance
     * @param account Accounts.Account number of the recepient.
     * @param amount Amount of money to be transferred.
     * @return true if transaction is successful, else false
     * @throws IllegalAccountType if account argument is no a Saving account
     */
    @Override
    public boolean transfer(Account account, double amount) throws IllegalAccountType {
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalAccountType("You cannot transfer to a non-savings account.");
        }

        // Making sure if the given account exist in the same bank as this account
        SavingsAccount recipient = (SavingsAccount) getBank().getBankAccount(getBank(), account.getAccountNumber());

        if (recipient == null) {
            System.out.println("The recipient account does not exist in this bank.");
        }
        else if (amount > getBank().getWithdrawLimit()) {
            System.out.println("The amount you are trying to transfer is too large for the bank.");
        }
        else if (!hasEnoughBalance(amount)){
            this.insufficientBalance();
        }
        else{
            recipient.adjustBalance(amount);
            this.adjustBalance(-amount);

            //Saving the transaction in this account's logs
            String description = String.format("Transfer $%f from %s Account No.%s to  %s Account No.%s", amount, getBank(),this.getAccountNumber(), getBank() ,recipient.getAccountNumber());
            addNewTransaction(this.getAccountNumber(), Transaction.Transactions.FundTransfer, description);
            recipient.addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Deposit, description);

            return true;
        }
        return false;
    }

    /**
     * Deposit to this account if the amount did not exceed to the limit
     * @param amount Amount to be deposited.
     * @return true if successful, else false
     */
    @Override
    public boolean cashDeposit(double amount) {
        if (amount > getBank().getDepositLimit()) {
            System.out.println("The amount you are trying to deposit is too large for the bank.");
        }
        else {
            adjustBalance(amount);

            //Saving the transaction
            String description = String.format("Deposit $%f", amount);// transaction description
            addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Deposit, description);
            return true;
        }
        return false;
    }

    /**
     * Withdraws money from this account if amount did not exceed to the limit
     * Saves the transaction
     * @param amount Amount of money to be withdrawn from.
     * @return true if successful, else false
     */
    @Override
    public boolean withdrawal(double amount) {
        if (amount > getBank().getWithdrawLimit()) {
            System.out.println("The amount you are trying to withdraw is too large for the bank.");
        }
        else if (!hasEnoughBalance(amount)){
            this.insufficientBalance();
        }
        else{
            adjustBalance(-amount);

            // Saving the transaction
            String description = String.format("Withdraw $%f", amount);;
            addNewTransaction(this.getAccountNumber(), Transaction.Transactions.Withdraw, description);
            return true;
        }
        return false;
    }

    /**
     * Adjust based on the amount given
     * @param amount could be positive or negative depends on the use
     */
    private void adjustBalance(double amount) {
        this.balance += amount;

        if (this.balance < 0) {
            this.balance = 0.0d;
        }
    }

    public String toString() {
        return super.toString() + "\n\t\tCurrent Balance Amount: " + getAccountBalanceStatement();
    }
}

