package Accounts;

import Bank.*;
import Main.*;

public class SavingsAccountLauncher extends AccountLauncher {

    // Method to initialize a savings account
    public static void savingsAccountInit(){

        while(true){
            Main.showMenuHeader("Savings Account Main Menu");
            Main.showMenu(51);
            Main.setOption();

            if (Main.getOption() == 1){
                double balance = getLoggedAccount().getAccountBalance();
                System.out.println("Current balance: $" + balance);
            }
            else if (Main.getOption() == 2){
                depositProcess();
            }
            else if (Main.getOption() == 3){
                withdrawProcess();
            }
            else if (Main.getOption() == 4){
                fundTransferProcess();
            } else if (Main.getOption() == 5) {
                System.out.println("<-----Transactions----->");
                String transaction_log = getLoggedAccount().getTransactionsInfo();
                System.out.println(transaction_log);
            }
            else if (Main.getOption() == 6){
                break;
            }
            else{
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    // Method for the deposit process
    private static void depositProcess() {
        Field<Double, Double> amountField = new Field<Double, Double>("amount",
                Double.class, 0.0, new Field.DoubleFieldValidator());
        amountField.setFieldValue("Enter deposit amount: ");
        double depositAmount = amountField.getFieldValue();

        boolean depositSuccess = getLoggedAccount().cashDeposit(depositAmount);
        if (depositSuccess) {
            System.out.println("Deposit of $" + depositAmount + " processed successfully.");
        } else {
            System.out.println("Deposit failed.");
        }
    }

    // Method for the withdrawal process
    private static void withdrawProcess() {
        Field<Double, Double> amountField = new Field<Double, Double>("amount",
                Double.class, 0.0, new Field.DoubleFieldValidator());
        amountField.setFieldValue("Enter withdrawal amount: ");
        double withdrawAmount = amountField.getFieldValue();

        boolean withdrawSuccess = getLoggedAccount().withdrawal(withdrawAmount);
        if (withdrawSuccess) {
            System.out.println("Withdrawal of $" + withdrawAmount + " processed successfully.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    // Method for the fund transfer process
    private static void fundTransferProcess(){
        Field<Integer, Integer> bankID = new Field<Integer,Integer>("ID", Integer.class, -1, new Field.IntegerFieldValidator());
        Field<String, String> bankName = new Field<String,String>("Name", String.class, "", new Field.StringFieldValidator());
        bankID.setFieldValue("Enter bank id: ");
        bankName.setFieldValue("Enter bank name: ");

        Bank target_bank = null;

        for (Bank bank : BankLauncher.banks) {
            if (bank.getID() == bankID.getFieldValue() && bank.getName().equals(bankName.getFieldValue())) {
                System.out.println("Bank selected: " + bankName.getFieldValue());
                target_bank = bank;
            }
        }

        if (target_bank == null) {
            System.out.println("Invalid bank id or name.");
            return;
        }

        Field<String, String> targetAccountNumberField = new Field<String, String>("targetAccountNumber",
                String.class, " ", new Field.StringFieldValidator());
        targetAccountNumberField.setFieldValue("Enter target account number: ");

        Field<Double, Double> amountField = new Field<Double, Double>("amount",
                Double.class, 0.0, new Field.DoubleFieldValidator());
        amountField.setFieldValue("Enter transfer amount: ");

        Account targetAccount = target_bank.getBankAccount(target_bank, targetAccountNumberField.getFieldValue());

        if (targetAccount == getLoggedAccount()) {
            System.out.println("Fund transfer to the same account is not allowed.");
            return;
        }

        if (getLoggedAccount().getBank().getID() == target_bank.getID()) {
            try {
                boolean transferSuccess = getLoggedAccount().transfer(targetAccount, amountField.getFieldValue());
                if (transferSuccess) {
                    System.out.println("Fund transfer of $" + amountField.getFieldValue() + " processed successfully.");
                }
            } catch (IllegalAccountType e) {
                throw new RuntimeException(e);
            }

        }
        else {
            try {
                boolean transferSuccess = getLoggedAccount().transfer(target_bank, targetAccount, amountField.getFieldValue());
                if (transferSuccess) {
                    System.out.println("Fund transfer of $" + amountField.getFieldValue() + " processed successfully.");
                }
            } catch (IllegalAccountType e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Method to get the logged savings account
    protected static SavingsAccount getLoggedAccount() {
        return (SavingsAccount) AccountLauncher.getLoggedAccount();
    }
}
