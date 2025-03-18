import Accounts.Account;
import Accounts.CreditAccount;
import Accounts.SavingsAccount;
import Bank.Bank;
import Bank.BankLauncher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TestBank {

    private final InputStream or = System.in;

    @BeforeEach
    public void reset() {
        System.setIn(or);
    }

    @AfterEach
    public void restore() {
        System.setIn(or);
    }

    @Test
    public void test1() {
        InputStream original = System.in;

        try {
            // Test user input
            String input = "Land Bank of the Philippines\n12345678\n50000.0\n50000.0\n100000.0\n10.0\n";

            ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
            // Let System feed the testInput above into the Scanner inputs when they are called
            System.setIn(testInput);

            BankLauncher.createNewBank();

            Bank bank = BankLauncher.getBank(new Bank.BankIdComparator(), new Bank(0, "Land Bank of the Philippens", "12345678"));

            Assert.assertEquals("Land Bank of the Philippines", bank.getName());
            Assert.assertEquals(100000.0, bank.getCreditLimit(), 0.00001);
            Assert.assertEquals(50000.0, bank.getDepositLimit(), 0.00001);
            Assert.assertEquals(50000.0, bank.getDepositLimit(), 0.00001);
            Assert.assertEquals(1, BankLauncher.bankSize());
        } finally {
            System.setIn(original);
        }
    }

    @Test
    public void test2() {
        InputStream original = System.in;

        try {
            String in1 = "Land Bank of the Philippines\n12345678\n50000.0\n50000.0\n100000.0\n10.0\n";
            String in2 = "Iglesia ni Dulay\n001122\n75000.0\n75000.0\n150000.0\n10.0\n";

            String myinput = in1 + in2;

            ByteArrayInputStream instream1 = new ByteArrayInputStream(myinput.getBytes());

            // Create first bank
            System.setIn(instream1);
            BankLauncher.createNewBank();
            // Create second bank
            BankLauncher.createNewBank();

            // Get two banks
            Bank bank1 = BankLauncher.getBank(new Bank.BankIdComparator(), new Bank(0, null, null));
            Bank bank2 = BankLauncher.getBank(new Bank.BankIdComparator(), new Bank(1, null, null));

            Assert.assertEquals(2, BankLauncher.bankSize());

            // Test Bank 1 values
            Assert.assertEquals("Land Bank of the Philippines", bank1.getName());
            Assert.assertEquals(100000.0, bank1.getCreditLimit(), 0.00001);
            Assert.assertEquals(50000.0, bank1.getDepositLimit(), 0.00001);
            Assert.assertEquals(50000.0, bank1.getDepositLimit(), 0.00001);

            // Test Bank 2 values
            Assert.assertEquals("Iglesia ni Dulay", bank2.getName());
            Assert.assertEquals(150000.0, bank2.getCreditLimit(), 0.00001);
            Assert.assertEquals(75000.0, bank2.getDepositLimit(), 0.00001);
            Assert.assertEquals(75000.0, bank2.getDepositLimit(), 0.00001);
        } finally {
            System.setIn(original);
        }
    }

    // Test Create an Account
    @Test
    public void test3() {
        InputStream original = System.in;

        try {
            // Create bank input
            String in1 = "Land Bank of the Philippines\n12345678\n50000.0\n50000.0\n100000.0\n10.0\n";
            // Log in bank
            String in2 = "Land Bank of the Philippines\n12345678\n";
            // Create savings
            String in3 = "2\n2\n";
            // Manual Savings input
            String in4 = "20010-00001\n1234\nJohn\nDoe\njd@gmail.com\n500.0\n";
            // Creating credit
            String in5 = "2\n1\n";
            // Manual Credit Input
            String in6 = "20010-00002\n1234\nJane\nDoe\njaned@gmail.com\n";
            // Get both accounts
            String in7 = "3\n";

            String input = in1 + in2 + in3 + in4 + in5 + in6 + in7;

            ByteArrayInputStream instream = new ByteArrayInputStream(input.getBytes());

            System.setIn(instream);
            BankLauncher.createNewBank();
            // Do stuff
            BankLauncher.bankLogin();

            // Get accounts after all of that
            Account saccount = BankLauncher.findAccount("20010-00001");
            Account caccount = BankLauncher.findAccount("20010-00002");

            Assert.assertEquals("John Doe", saccount.getOwnerFullName());
            Assert.assertEquals(500.0, ((SavingsAccount) saccount).getAccountBalance(), 0.00001);
            Assert.assertEquals("jd@gmail.com", saccount.getOwnerEmail());

            Assert.assertEquals("Jane Doe", caccount.getOwnerFullName());
            Assert.assertEquals(0.0, ((CreditAccount) caccount).getLoan(), 0.00001);
            Assert.assertEquals("janed@gmail.com", caccount.getOwnerEmail());
        } finally {
            System.setIn(original);
        }
    }
}
