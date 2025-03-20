package Bank;

import Accounts.*;
import Main.Field;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 *Bank class
 */
public class Bank {

    /***
     * ID for bank account I guess, not really sure for now
     */
    private int ID;

    /**
     * Name and password of the user account
     */
    private String name, passcode;

    /**
     * The most amount the user can deposit
     */
    private double depositLimit;

    /**
     * The most amount the user can withdraw
     */
    private double withdrawLimit;

    /**
     * Limit amount of all Credit Account can loan
     */
    private double creditLimit;

    /**
     * Fee added when there is other bank transaction
     */
    private double processingFee;

    /**
     * Stores all the account in this bank
     */
    private ArrayList<Account> bankAccounts;
    private static final Scanner input = new Scanner(System.in);

    public Bank(int ID, String name, String passcode) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
        this.depositLimit = 50000.00d;
        this.withdrawLimit = 50000.00d;
        this.creditLimit = 100000.00d;
        this.processingFee = 10.00d;
        bankAccounts = new ArrayList<>();
    }

    public Bank(int ID, String name, String passcode, double depositLimit, double withdrawLimit, double creditLimit, double processingFee) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
        this.depositLimit = depositLimit;
        this.withdrawLimit = withdrawLimit;
        this.creditLimit = creditLimit;
        this.processingFee = processingFee;
        bankAccounts = new ArrayList<>();
    }

    /**
     * Retrieves the bank ID
     * @return  the bank ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Retrieves the bank name
     * @return  Bank Name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the passcode for the bank.
     * @return the passcode of the bank
     */
    public String getPasscode() {
        return passcode;
    }

    /**
     * Retrieves the deposit limit
     * @return the deposit limit of the bank
     */
    public double getDepositLimit() {
        return depositLimit;
    }

    /**
     * Retrieves the withdraw limit of the bank.
     * @return the withdraw limit of the bank
     */
    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    /**
     * Retrieves the credit limit of the bank.
     * @return the credit limit of the bank
     */
    public double getCreditLimit() {
        return creditLimit;
    }

    /**
     * Retrieves the processing fee associated with the bank.
     *
     * @return the processing fee of the bank as a double.
     */
    public double getProcessingFee() {
        return processingFee;
    }

    /**
     * Shows account based on its type
     *
     * @param accountType
     */
    public void showAccounts(Class accountType) {

        for (int i = 0; i < bankAccounts.size(); i++) {
            if (accountType.isInstance(bankAccounts.get(i))) {
                System.out.printf("[%d]. %s\n", i+1, bankAccounts.get(i));
            }
        }
    }

    /**
     * Search for a specific account in a specific bank
     *
     * @param bank
     * @param accountNumber
     * @return Account if it had similar bank and account number
     */
    public Account getBankAccount(Bank bank, String accountNumber) {
        for (int i = 0; i < bank.bankAccounts.size(); i++) {
            Account curr_account = bank.bankAccounts.get(i);
            if (curr_account.getAccountNumber().equals(accountNumber)) {
                return bank.bankAccounts.get(i);
            }
        }
        return null;
    }

    /**
     * taking Inputs
     *
     * @param phrase
     * @return
     */

    /**
     * Takes the string arguments needed for an account
     * @return String arguments for an account
     */
    public ArrayList<Field<String, String>> createNewAccount() {
            Field<String, String> accountNumber = new Field<String, String>("Bank Name",
                    String.class, " ", new Field.StringFieldValidator());
            accountNumber.setFieldValue("Enter account number: ");

        Field<String, String> accountPIN = new Field<String, String>("Bank Name",
                String.class, " ", new Field.StringFieldValidator());
        accountPIN.setFieldValue("Enter PIN: ", false);

            Field<String, String> ownerFname = new Field<String, String>("Bank Name",
                    String.class, " ", new Field.StringFieldValidator());
            ownerFname.setFieldValue("Enter owner firstname: ", false);

            Field<String, String> ownerLname = new Field<String, String>("Bank Name",
                    String.class, " ", new Field.StringFieldValidator());
            ownerLname.setFieldValue("Enter owner lastname: ", false);

            Field<String, String> ownerEmail = new Field<String, String>("Bank Name",
                    String.class, " ", new Field.StringFieldValidator());
            ownerEmail.setFieldValue("Enter owner email: ", false);


        return new ArrayList<>(Arrays.asList(
                accountNumber, accountPIN, ownerFname, ownerLname, ownerEmail));
    }

    /**
     * Creates a new credit account for the bank by gathering required information,
     * initializing a CreditAccount instance with the provided details, and adding
     * it to the bank's account list.
     * @return the newly created CreditAccount instance
     */
    public CreditAccount createNewCreditAccount(){
        ArrayList<Field<String, String>> accountInfo = this.createNewAccount();
        String accountNumber = accountInfo.get(0).getFieldValue();
        String firstName = accountInfo.get(1).getFieldValue();
        String lastName = accountInfo.get(2).getFieldValue();
        String email = accountInfo.get(3).getFieldValue();
        String pin = accountInfo.get(4).getFieldValue();

        //Checks if an account with the same account number already exist in this bank
        if(accountExists(this, accountNumber)){
            System.out.println("Account already exists!");
            return null;
        }

        CreditAccount creditAccount = new CreditAccount(this, accountNumber, firstName, lastName, email, pin);
        this.addNewAccount(creditAccount);
        return creditAccount;
    }

    /**
     * Creates a new savings account for the bank by gathering required information,
     * initializing a SavingsAccount instance with the provided details,
     * and adding it to the bank's account list.
     * @return the newly created SavingsAccount instance
     */
    public SavingsAccount createNewSavingsAccount(){
        ArrayList<Field<String, String>> accountInfo = this.createNewAccount();
        String accountNumber = accountInfo.get(0).getFieldValue();
        String firstName = accountInfo.get(1).getFieldValue();
        String lastName = accountInfo.get(2).getFieldValue();
        String email = accountInfo.get(3).getFieldValue();
        String pin = accountInfo.get(4).getFieldValue();
        Field<Double,Double> balanceField = new Field<Double, Double>("Deposit Limit", Double.class, 0.0, new Field.DoubleFieldValidator());
        balanceField.setFieldValue("Enter account balance: ");
        double balance = balanceField.getFieldValue();

        //Checks if an account with the same account number already exist in this bank
        if(accountExists(this, accountNumber)){
            System.out.println("Account already exists!");
            return null;
        }

        SavingsAccount savingsAccount = new SavingsAccount(this,accountNumber, firstName, lastName, email, pin, balance);
        this.addNewAccount(savingsAccount);
        return savingsAccount;
    }

    /**
     * Adding accounts to a bank
     * @param account
     */
    public void addNewAccount(Account account) {
        if (!accountExists(this, account.getAccountNumber())) {
            bankAccounts.add(account);
        }

    }

    /**
     * checks if an account exist in the bank
     * @param bank
     * @param accountNumber
     * @return
     */
    public static boolean accountExists(Bank bank, String accountNumber) {
        Account account = bank.getBankAccount(bank, accountNumber);
        return account != null;
    }

    /**
     * Sting representation of a bank
     * @return the bank's string representation
     */
    public String toString() {
        return String.format(name);
    }

    public static class BankComparator implements Comparator<Bank> {
        @Override
        public int compare(Bank b1, Bank b2) {
            return b1.equals(b2) ? 0 : 1;
        }
    }

    public static class BankCredentialsComparator implements Comparator<Bank> {
        @Override
        public int compare(Bank b1, Bank b2) {
            return (b1.getName().equals(b2.getName()) && b1.getPasscode().equals(b2.getPasscode())) ? 0 : 1;
        }
    }

    public static class BankIdComparator implements Comparator<Bank>{
        @Override
        public int compare(Bank b1, Bank b2) {
            return b1.getID() == b2.getID() ? 0 : 1;
        }
    }

}




