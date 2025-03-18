import Accounts.CreditAccount;
import Accounts.IllegalAccountType;
import Accounts.SavingsAccount;
import Bank.BankLauncher;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestCredit {

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
        // Creating credit account #1
        String in5 = "2\n1\n";
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
            CreditAccount caccount1 = (CreditAccount) BankLauncher.findAccount("20010-00002");

            saccount1.cashDeposit(10000.0);
            Assert.assertEquals(10500.0, saccount1.getAccountBalance(), 0.00001);

            try {
                caccount1.pay(saccount1, 1500.0);
                Assert.assertEquals(12000.0, saccount1.getAccountBalance(), 0.00001);
                Assert.assertEquals(1500.0, caccount1.getLoan(), 0.00001);

                caccount1.recompense(1000.0);
                Assert.assertEquals(500.0, caccount1.getLoan(), 0.00001);

            } catch (IllegalAccountType e) {
            }

            String saccount1Log = saccount1.getTransactionsInfo();
            int sa1LogCount = saccount1Log.isEmpty() ? 0 : saccount1Log.split("\n").length;

            String caccount1Log = caccount1.getTransactionsInfo();
            int ca1LogCount = caccount1Log.isEmpty() ? 0 : caccount1Log.split("\n").length;

            Assert.assertEquals(3, sa1LogCount);
            Assert.assertEquals(2, ca1LogCount);

        } finally {
            System.setIn(original);
        }
    }
}
