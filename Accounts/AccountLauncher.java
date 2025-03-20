package Accounts;

import Bank.*;
import Main.*;

public class AccountLauncher {
    private static Account loggedAccount;
    private static Bank assocBank;

    // is loggedIn
    private static boolean isLoggedIn() {
        return loggedAccount != null;
    }

    public static void accountLogin(){
        // If bank is found
        // After bank is found and there is no logged user
        // 1. Prompt for account number and passcode
        // 2. After prompting, look for every account in the associated bank and check if that account exists
        // 3. If it exists, return the account object and call setLogSession() method
        // 4. Log in, call the init() method of the Account...
        // 4.1 If the account is a CreditAccount, call creditAccountInit()
        // 4.2 else if the account is a SavingsAccount, call savingsAccountInit()
        // If bank is found but there is someone logged in
        // 1. Call AccountLauncher init() method

        destroyLogSession();
        assocBank = null;

        while (assocBank == null) {
//            System.out.println("Select a bank first");
            assocBank = selectBank();
            if (assocBank == null) {
                System.out.println("Invalid bank selection. Please try again.");
            }
        }

        Main.showMenuHeader("Account Login");
        String accountNum = Main.prompt("Enter account number: ", true);
        String pin = Main.prompt("Enter  PIN: ", true);

        loggedAccount = checkCredentials(accountNum, pin);
        if (loggedAccount != null) {
            System.out.println("Login successful.");
            setLogSession(loggedAccount);
            if (loggedAccount.getClass() == SavingsAccount.class) {
                SavingsAccountLauncher.savingsAccountInit();
            }
            else if (loggedAccount.getClass() == CreditAccount.class) {
                CreditAccountLauncher.creditAccountInit();
            }
        }
        else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    // Bank selectBank
    private static Bank selectBank() {
        BankLauncher.showBanksMenu();
        Field<String, String> bankName = new Field<String,String>("Name", String.class, "", new Field.StringFieldValidator());
        bankName.setFieldValue("Enter bank name: ");

        for (Bank bank : BankLauncher.banks) {
            if (bank.getName().equals(bankName.getFieldValue())) {
                System.out.println("Bank selected: " + bankName.getFieldValue());
                return bank;
            }
        }
        return null;
    }


    // set log session

    private static void setLogSession(Account account) {
        loggedAccount = account;
        System.out.println("Logged-in account set: " + account.getOwnerFullName());
    }

    // destroy log session

    private static void destroyLogSession() {
        if (isLoggedIn()) {
            loggedAccount = null;
        }
    }

    // check credentials
    public static Account checkCredentials(String accountNum, String pin) {
        Account account = BankLauncher.findAccount(accountNum);
        if (account != null && account.getPin().equals(pin)) {
            return account;
        }
        return null;
    }

    // #get logged account
    protected static Account getLoggedAccount() {
        return loggedAccount;
    }
}