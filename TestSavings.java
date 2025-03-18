import Accounts.CreditAccount;
import Accounts.IllegalAccountType;
import Accounts.SavingsAccount;
import Bank.Bank;
import Bank.BankLauncher;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestSavings {

    /**
     * Test fund transfer in 1 same bank
     */
    @Test
    public void test1() {

        // Create bank input
        String in1 = "Land Bank of the Philippines\n12345678\n50000.0\n50000.0\n100000.0\n10.0\n";
        // Log in bank
        String in2 = "Land Bank of the Philippines\n12345678\n";
        // Create savings account #1
        String in3 = "2\n2\n";
        // Manual Savings input
        String in4 = "20010-00001\n1234\nJohn\nDoe\njd@gmail.com\n500.0\n";
        // Creating savings account #2
        String in5 = "2\n2\n";
        // Manual Credit Input
        String in6 = "20010-00002\n1234\nJane\nDoe\njaned@gmail.com\n1000.0\n";
        // Logout
        String in7 = "3\n";

        String input = in1 + in2 + in3 + in4 + in5 + in6 + in7;

        InputStream original = System.in;

        try {
            ByteArrayInputStream instream = new ByteArrayInputStream(input.getBytes());

            System.setIn(instream);
            BankLauncher.createNewBank();
            // Do stuff
            BankLauncher.bankLogin();

            SavingsAccount saccount1 = (SavingsAccount) BankLauncher.findAccount("20010-00001");
            SavingsAccount saccount2 = (SavingsAccount) BankLauncher.findAccount("20010-00002");

            saccount1.cashDeposit(1000.0);
            Assert.assertEquals(1500.0, saccount1.getAccountBalance(), 0.00001);
            saccount2.cashDeposit(10000.0);
            Assert.assertEquals(11000.0, saccount2.getAccountBalance(), 0.00001);
            Assert.assertEquals(false, saccount2.cashDeposit(1000000000.0));
            try {
                saccount2.transfer(saccount1, 5000.0);
                Assert.assertEquals(6000.0, saccount2.getAccountBalance(), 0.00001);
                Assert.assertEquals(6500.0, saccount1.getAccountBalance(), 0.00001);

                Assert.assertThrows(IllegalAccountType.class, () -> {
                    saccount2.transfer(new CreditAccount(new Bank(1, "Test","2323"), null, null, null, null, null), 2000.0);
                });
            } catch(IllegalAccountType exception) {
            }

            String saccount2logs = saccount2.getTransactionsInfo();
            int sacc2logCount = saccount2logs.isEmpty() ? 0 : saccount2logs.split("\n").length;

            String saccount1logs = saccount1.getTransactionsInfo();
            int sacc1logCount = saccount1logs.isEmpty() ? 0 : saccount1logs.split("\n").length;

            Assert.assertEquals(3, sacc2logCount);
            Assert.assertEquals(3, sacc1logCount);
        } finally {
            System.setIn(original);
        }
    }

    /**
     * Test fund transfer on 2 different banks
     */
    @Test
    public void test2() {
        // Create bank 1 input
        String in1 = "BDO\naabbccdd\n50000.0\n50000.0\n100000.0\n10.0\n";
        // Create bank 2 input
        String bn2 = "BDP\n12345667\n75000.0\n75000.0\n150000.0\n15.0\n";
        // Log in bank 1
        String in2 = "BDO\naabbccdd\n";
        // Create savings account #1 for bank #1
        String in3 = "2\n2\n";
        // Manual Savings 1 input
        String in4 = "20010-00001\n1234\nJohn\nDoe\njd@gmail.com\n500.0\n";
        // Creating credit account #1 for bank #1
        String in5 = "2\n1\n";
        // Manual Credit Account 1 Input
        String in6 = "20010-00002\n1234\nJane\nDoe\njaned@gmail.com\n";
        // Logout
        String in7 = "3\n";

        // Login on Bank 2
        String log2 = "BDP\n12345667\n";
        // Creating savings in Bank 2
        String acc2 = "2\n2\n";
        // Manual Bank 2 Savings input
        String acc3 = "20011-00001\n5566\nJose\nRizel\njprizal@gmail.com\n5000.0\n";
        String logout2 = "3\n";

        String input = in1 + bn2 + in2 + in3 + in4 + in5 + in6 +
                in7 + log2 + acc2 + acc3 + logout2;

        InputStream original = System.in;

        try {
            ByteArrayInputStream instream = new ByteArrayInputStream(input.getBytes());

            System.setIn(instream);
            BankLauncher.createNewBank();
            BankLauncher.createNewBank();
            // Do stuff for first bank
            BankLauncher.bankLogin();
            // Do stuff on second bank
            BankLauncher.bankLogin();

            Bank bdo = BankLauncher.getBank(new Bank.BankIdComparator(), new Bank(0, null, null));
            Bank bdp = BankLauncher.getBank(new Bank.BankIdComparator(), new Bank(1, null, null));

            SavingsAccount bank1sa = (SavingsAccount) BankLauncher.findAccount("20010-00001");
            SavingsAccount bank2sa = (SavingsAccount) BankLauncher.findAccount("20011-00001");
            CreditAccount bank1ca = (CreditAccount) BankLauncher.findAccount("20010-00002");

            bank1sa.cashDeposit(5000.0);
            Assert.assertEquals(5500.0, bank1sa.getAccountBalance(), 0.00001);
            try {
                bank1sa.transfer(bank2sa.getBank(), bank2sa, 4000.0);
                Assert.assertEquals(9000.0, bank2sa.getAccountBalance(), 0.00001);
                Assert.assertEquals(5500.0 - 4000.0 - bdo.getProcessingFee(), bank1sa.getAccountBalance(), 0.00001);
                Assert.assertThrows(IllegalAccountType.class, () -> {
                    bank1sa.transfer(bank1ca, 1000.0);
                });

                // Check logs

                String bank1saLogs = bank1sa.getTransactionsInfo();
                int bank1saCount = bank1saLogs.isEmpty() ? 0 : bank1saLogs.split("\n").length;

                String bank2saLogs = bank2sa.getTransactionsInfo();
                int bank2saCount = bank2saLogs.isEmpty() ? 0 : bank2saLogs.split("\n").length;

                String bank1caLogs = bank1ca.getTransactionsInfo();
                int bank1caCount = bank1caLogs.isEmpty() ? 0 :bank1caLogs.split("\n").length;

                Assert.assertEquals(3, bank1saCount);
                Assert.assertEquals(2, bank2saCount);
                Assert.assertEquals(1, bank1caCount);
            } catch (IllegalAccountType exception) {

            }
        } finally {
            System.setIn(original);
        }
    }
}
