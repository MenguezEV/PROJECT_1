package Accounts;

import Main.*;
import Bank.BankLauncher;

public class CreditAccountLauncher extends AccountLauncher {

    public static void creditAccountInit() {

        while(true){
            Main.showMenuHeader(String.format("%s Credit Account No.%s", getLoggedAccount().getBank(),getLoggedAccount().getAccountNumber()));
            Main.showMenu(41);
            Main.setOption();

            if (Main.getOption() == 1){
                String loan_statement = getLoggedAccount().getLoanStatement();
                System.out.println(loan_statement);
            }
            else if (Main.getOption() == 2){
                creditPaymentProcess();
            }
            else if (Main.getOption() == 3){
                creditRecompenseProcess();
            }
            else if (Main.getOption() == 4){
                Main.showMenuHeader("Transactions");
                String transaction_log = getLoggedAccount().getTransactionsInfo();
                System.out.println(transaction_log);
            }
            else if (Main.getOption() == 5){
                break;
            }
            else{
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void creditPaymentProcess() {

        Field<String, String> targetaccountnumberField = new Field<String, String>("targetAccount",
                String.class, " ", new Field.StringFieldValidator());
        targetaccountnumberField.setFieldValue("Enter target account number: ");
        String accountNumber = targetaccountnumberField.getFieldValue();

        Field<Double, Double> amountField = new Field<Double, Double>("amount",
                Double.class, 0.0, new Field.DoubleFieldValidator());
        amountField.setFieldValue("Enter payment ammount: ");
        double amountToPay = amountField.getFieldValue();

        Account targetAccount = BankLauncher.findAccount(accountNumber);
        double processingFee = getLoggedAccount().getBank().getProcessingFee();
        double payAmountWithFee = amountToPay;
        // Put inside try catch block to catch the IllegalAccountType exception in pay() method
        if (targetAccount != null){
            try{
                boolean paySuccess = getLoggedAccount().pay(targetAccount, payAmountWithFee);

                if (paySuccess){
                    System.out.println("Payment of " + amountToPay + " processed successfully.");
                }
            }

            catch(IllegalAccountType e){
                System.out.println("Error: Illegal account type encountered while processing payment.");
            }
        }else{
            System.out.println("This account doesn't exist");
        }

    }

    private static void creditRecompenseProcess(){
        Field<Double, Double> amountField = new Field<Double, Double>("amount",
                Double.class, 0.0, new Field.DoubleFieldValidator());
        amountField.setFieldValue("Enter amount: ");
        double amountToPay = amountField.getFieldValue();

        boolean recompenseSuccess = getLoggedAccount().recompense(amountToPay);
        if (recompenseSuccess){
            System.out.println("Credit recompense of " + amountToPay + " processed successfully.");
        }
    }

    protected static CreditAccount getLoggedAccount(){
        return (CreditAccount) AccountLauncher.getLoggedAccount();
    }
}



