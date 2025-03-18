package Bank;


import Accounts.*;
import Main.*;
import java.util.ArrayList;
import java.util.Comparator;

public class BankLauncher {
    public static ArrayList<Bank> banks = new ArrayList<>();
    private static Bank loggedBank;

    public static boolean isLogged() {
        return loggedBank != null;
    }

    public static void bankInit() {
//        if(isLogged()){
//            Main.showMenuHeader("Bank Menu");
//            Main.showMenu(31);
//            Main.setOption();
//            switch (Main.getOption()){
//                case 1:
//                    showAccounts();
//                    break;
//                case 2:
//                    newAccounts();
//                    break;
//                case 3:
//                    logout();
//                default:
//                    System.out.println("Invalid Option. Please try again.");
//            }
//        }
        while(isLogged()){
            Main.showMenuHeader("Bank Menu");
            Main.showMenu(31);
            Main.setOption();

            if (Main.getOption() == 1) {
                showAccounts();
            } else if (Main.getOption() == 2) {
                newAccounts();
            } else if (Main.getOption() == 3) {
                logout();
            } else {
                System.out.println("Invalid Option. Please try again.");
            }
        }
    }

    private static void showAccounts() {
        if(isLogged()){
            Main.showMenu(32);
            Main.setOption();
            switch(Main.getOption()){
                case 1:
                    loggedBank.showAccounts(CreditAccount.class);
                    break;

                case 2:
                    //savings account
                    loggedBank.showAccounts(SavingsAccount.class);
                    break;

                case 3:
                    //all accounts
                    loggedBank.showAccounts(Account.class);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }

//        while (true) {
//            Main.showMenuHeader("Show Accounts Menu");
//            Main.showMenu(32);
//            Main.setOption();
//
//            if (Main.getOption() == 1) {
//                loggedBank.showAccounts(CreditAccount.class);
//            } else if (Main.getOption() == 2) {
//                loggedBank.showAccounts(SavingsAccount.class);
//            } else if (Main.getOption() == 3) {
//                loggedBank.showAccounts(Account.class);
//            } else if (Main.getOption() == 4) {
//                return;
//            } else {
//                System.out.println("Invalid input. Please try again.");
//            }
//        }
    }

    private static void newAccounts() {
        Main.showMenu(33);
        Main.setOption();

        switch(Main.getOption()){
            case 1:
                //new credit account
                CreditAccount newCreditAccount = loggedBank.createNewCreditAccount();
                if (newCreditAccount != null) {
                    System.out.println("Credit account with account number " + newCreditAccount.getAccountNumber() + " is created successfully!");
                }
                break;
            case 2:
                //new savings account
                SavingsAccount newSavingsAccount = loggedBank.createNewSavingsAccount();
                if (newSavingsAccount != null) {
                    System.out.println("Savings account with account number " + newSavingsAccount.getAccountNumber() + " is created successfully!");
                }
                break;
            default:
                System.out.println("Invalid input. Please try again.");
        }

    }

    public static void bankLogin() {
        Field<String, String> bankNameField = new Field<String, String>("Bank Name",
                String.class, " ", new Field.StringFieldValidator());
        bankNameField.setFieldValue("Enter bank name: ", false);
        String bankName = bankNameField.getFieldValue();

        Field<String, String> bankpasscodeField = new Field<String, String>("Bank Passcode",
                String.class, " ", new Field.StringFieldValidator());
        bankpasscodeField.setFieldValue("Enter bank passcode: ");
        String passcode = bankpasscodeField.getFieldValue();

        Bank matchedBank = null;

        int i = 0;
        while (matchedBank == null && i < bankSize()){
            matchedBank = getBank(new Bank.BankCredentialsComparator(), new Bank(i, bankName, passcode));
            i++;
        }

        if(matchedBank != null){
            setLogSession(matchedBank);
            bankInit();
        }
        else{
            System.out.println("Bank not found. Either invalid bank ID or passcode. Please try again.");
        }
//        bankInit();
    }

    private static void setLogSession(Bank b) {
        loggedBank = b;
    }

    private static void logout() {
        loggedBank = null;
    }

    public static void createNewBank() {
//        Field<Integer,Integer> idField = new Field<Integer, Integer>("ID", Integer.class, -1, new Field.IntegerFieldValidator());
        Field<String,String> nameField = new Field<String, String>("Name", String.class, "", new Field.StringFieldValidator());
        Field<String,Integer> passcodeField = new Field<String, Integer>("Passcode", String.class, 5, new Field.StringFieldLengthValidator());
        Field<Double,Double> depositLimitField = new Field<Double, Double>("Deposit Limit", Double.class, 0.0, new Field.DoubleFieldValidator());
        Field<Double,Double> withdrawLimitField = new Field<Double, Double>("Withdraw Limit", Double.class, 0.0, new Field.DoubleFieldValidator());
        Field<Double,Double> creditLimitField = new Field<Double, Double>("Credit Limit", Double.class, 0.0, new Field.DoubleFieldValidator());
        Field<Double,Double> processingFeeField = new Field<Double, Double>("Processing Fee", Double.class, 0.0, new Field.DoubleFieldValidator());

        try { //in creating new bank
//            idField.setFieldValue("Bank ID: ");
            nameField.setFieldValue("Bank Name: ", false);
            passcodeField.setFieldValue("Bank Passcode: ");
            //default amount -refer to bank 
            depositLimitField.setFieldValue("Set Deposit Limit by default? (Press 0 for default): ");
            withdrawLimitField.setFieldValue("Set Withdraw Limit by default? (Press 0 for default): ");
            creditLimitField.setFieldValue("Set Credit Limit by default? (Press 0 for default): ");
            processingFeeField.setFieldValue("set Processing Fee by default? (Press 0 for default): ");
        } catch (NumberFormatException e) {
            System.out.println("Invalid! Please enter a valid number.");
            return;
        }

//        int id = idField.getFieldValue();
        String name = nameField.getFieldValue();
        String passcode = passcodeField.getFieldValue();
        double depositLimit = depositLimitField.getFieldValue();
        double withdrawLimit = withdrawLimitField.getFieldValue();
        double creditLimit = creditLimitField.getFieldValue();
        double processingFee = processingFeeField.getFieldValue();

        Bank newBank;
        if (depositLimit == 0.0 && withdrawLimit == 0.0 && creditLimit == 0.0 && processingFee == 0.0) {
            newBank = new Bank(bankSize(), name, passcode);
        } else {
            newBank = new Bank(bankSize(), name, passcode, depositLimit, withdrawLimit, creditLimit, processingFee);
        }

//        System.out.println("Bank created successfully.");
        addBank(newBank);
    }

    public static void showBanksMenu() {
        System.out.println("List of Registered Banks: ");
        for(int i = 0; i < bankSize(); i++){
//            System.out.println(banks.get(i).getID() + " - " + banks.get(i).getName());
            System.out.printf("[%d]. Bank ID No.%d: %s\n", i + 1, banks.get(i).getID(), banks.get(i).getName());
        }
    }

    private static void addBank(Bank b) {
        // Check if a bank with the same ID already exists
        if (getBank(new Bank.BankIdComparator(), b) != null) {
            System.out.println("A bank with this ID already exists.");
            return;
        }
        banks.add(b);
        System.out.println("Bank added successfully.");
    }

    public static Bank getBank(Comparator<Bank> comparator, Bank bank) {
        // Loop to find bank if it exists using some comparator

        for (int i = 0; i < bankSize(); i++) {
            if (comparator.compare(bank, banks.get(i)) == 0) {
                return banks.get(i);
            }
        }
        return null; // Bank not found
    }


    public static Account findAccount(String accountNum) {
        for (int i = 0; i < bankSize(); i++) {
            if (Bank.accountExists(banks.get(i), accountNum)){

                return banks.get(i).getBankAccount(banks.get(i), accountNum);
            }
        }
        // If the account number is not found in any bank, return null
        return null;
    }

    public static int bankSize() {
        return banks.size();
    }


}