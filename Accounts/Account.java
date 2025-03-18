package Accounts;

import Bank.Bank;
import java.util.ArrayList;

public abstract class Account  {
    /**
     * Bank
     */
    private Bank bank;
    /**
     * Needed Feilds for an account
     */
    private String AccountNumber, ownerFName, ownerLName, ownerEmail, pin;
    /**
     * Where all transactions are recorded
     */
    private ArrayList<Transaction> Transactions;

    public Account(Bank bank, String AccountNumber, String ownerFName, String ownerLName, String ownerEmail, String pin) {
        this.bank = bank;
        this.AccountNumber = AccountNumber;
        this.ownerFName = ownerFName;
        this.ownerLName = ownerLName;
        this.ownerEmail = ownerEmail;
        this.pin = pin;
        Transactions = new ArrayList<>();
    }

    /**
     * Gets the Bank
     * @return Bank
     */
    public Bank getBank(){
        return bank;
    }

//    public abstract void setBank(Bank bank);

    /**
     * Gets the Account Number
     * @return String account number
     */
    public String getAccountNumber(){
        return AccountNumber;
    }

    /**
     * Change the Account number
     * @param AccountNumber
     */
    public void setAccountNumber(String AccountNumber){
        this.AccountNumber = AccountNumber;
    }

    /**
     * Get the owners first name
     * @return String Fname of the owner
     */
    public String getOwnerFName(){
        return ownerFName;
    }

    /**
     * Changes the owners fnmae
     * @param ownerFName
     */
    public void setOwnerFName(String ownerFName){
        this.ownerFName = ownerFName;
    }

    /**
     * Gets the owners Lastname
     * @return String Lastname of the owner
     */
    public String getOwnerLName(){
        return ownerLName;
    }
    /**
     * Changes the lastname of the owner
     * @param ownerLName
     */
    public void setOwnerLName(String ownerLName){
        this.ownerLName = ownerLName;
    }

    /**
     * Gets the email of the owner
     * @return String email of the owner
     */
    public String getOwnerEmail(){
        return ownerEmail;
    }

    /**
     * Changes the email of the owner
     * @param ownerEmail
     */
    public void setOwnerEmail(String ownerEmail){
        this.ownerEmail = ownerEmail;
    }

    /**
     * Gets the pin of the owner
     * @return
     */
    public String getPin(){
        return pin;
    }

    /**
     * Change pin
     * @param pin
     */
    public void setPin(String pin){
        this.pin = pin;
    }

    /**
     * Abstract method of combining the fname & lname
     * @return String
     */
    public String getOwnerFullName(){
        return ownerFName + " " + ownerLName;
    }

//    /**
//     * Retrieves the list of transactions associated with this account.
//     *
//     * @return ArrayList of Transaction objects representing the transactions of this account.
//     */
//    public ArrayList<Transaction> getTransactions(){
//        return Transactions;
//    }

    /**
     * Create a Transaction and put it in the Transactions for recording
     * @param accountNum
     * @param transactionType
     * @param desciption
     */
    public void addNewTransaction(String accountNum, Transaction.Transactions transactionType, String desciption){
        Transaction transaction = new Transaction(accountNum, transactionType, desciption);
        Transactions.add(transaction);
    }

    /**
     * Shows every transaction this account have
     * @return A string of all the transaction this account have
     */
    public String getTransactionInfo(){
        if (Transactions.isEmpty()) return "No transactions";

        String s = "===Transactions===\n";
        for(int i = 0; i<Transactions.size(); i++){
//            s += Transactions.get(i).description + "\n";
            s += String.format("[%d]. %s\n", i+1, Transactions.get(i).description);
        }
        return s;
    }

    /**
     * Shows the details of this account
     * @return The account Details
     */
    public String toString(){
        return String.format("Account No.%s Owner: %s", this.getAccountNumber(), this.getOwnerFullName());
    }
}
