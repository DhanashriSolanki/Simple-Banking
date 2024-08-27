import java.awt.BorderLayout; //Abstract Window Toolkit --> lightweight API --> depends entirely on java
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

interface UIUpdatable {
    void updateUI();
}

class Account implements UIUpdatable {
    private String accountNumber;
    private String name;
    private double balance; // these variables have been made private for encapsulation

    public Account(String accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    // the below methods return the value of private variables, they are not used
    // directly
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. Current balance: " + balance);
        updateUI();
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds. Withdrawal failed.");
        } else {
            balance -= amount;
            System.out.println("Withdrawal successful. Current balance: " + balance);
            updateUI();
        }
    }

    // the updateUI method prints the message in the terminal

    public void updateUI() {

        System.out.println("UI Updated for Account Number: " + accountNumber);
    }
} // end of banking functions

// JFrame provides the frame for the ui components to be displayed
class BankingUI extends JFrame {
    private JTextField accountNumberField; // single line text input
    private JTextField amountField;
    private JTextArea outputArea; // display

    private Account[] accounts;

    public BankingUI(Account[] accounts) {
        this.accounts = accounts;

        // these are components of JFrame class
        setTitle("Banking System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // application exits

        initialize_components_of_ui();
    }

    private void initialize_components_of_ui() {
        // frame-->panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); // arranging in grids using GridLayout class

        // labels and fields will be added into the grids
        panel.add(new JLabel("Account Number:")); // JLabel --> built in class
        accountNumberField = new JTextField();
        panel.add(accountNumberField);
        accountNumberField.setBackground(Color.pink);

        panel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        amountField.setBackground(Color.pink);
        panel.add(amountField);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBackground(Color.BLACK);
        depositButton.setForeground(Color.white);
        depositButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) { // ActionEvent --> class
                performTransaction(true);
            }
        });
        panel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(Color.BLACK);
        withdrawButton.setForeground(Color.white);
        withdrawButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) { // method overriding
                performTransaction(false);
            }
        });
        panel.add(withdrawButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false); // prevents user from editing
        outputArea.setBackground(Color.pink);

        add(panel, BorderLayout.NORTH); // displays at top
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void performTransaction(boolean isDeposit) {
        String accountNumber = accountNumberField.getText().trim(); // trim removes whitespaces
        String amountText = amountField.getText().trim();

        if (!accountNumber.isEmpty() && !amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText); // wrapper class --> converts to double
            Account account = findAccount(accountNumber);

            if (account != null) {
                if (isDeposit) {
                    account.deposit(amount);
                    displayAccountInfo(account);
                } else {
                    if (amount <= account.getBalance()) {
                        account.withdraw(amount);
                        displayAccountInfo(account);
                    } else {

                        outputArea.append("Insufficient balance. Account details are not changed\n");
                        displayAccountInfo(account);
                    }
                }
            } else {
                outputArea.append("Account not found.\n");
            }
        } else {
            outputArea.append("Please enter both account number and amount.\n");
        }
    }

    private Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private void displayAccountInfo(Account account) {
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.toString();

        // append is built in method to append the following to the area
        outputArea.append("Account Number: " + account.getAccountNumber() + "\n");
        outputArea.append("Account Holder Name: " + account.getName() + "\n");
        outputArea.append("Balance: " + account.getBalance() + "\n");
        outputArea.append("Date of transaction: " + today + "\n");
        outputArea.append("\n");
    }

}

public class SimpleBankingSystem {
    public static void main(String[] args) {

        // array of accounts
        Account[] accounts = {
                new Account("123456", "Alex Doe", 10000.0),
                new Account("111213", "Tristain Doe", 5000.0),
                new Account("141516", "Christian Alonso", 2000.0),
                new Account("171819", "Stella Chan", 1000.0),
                new Account("202122", "Josh Chan", 52000.0),
                new Account("232425", "Zade Rei", 25000.0)
        };

        // create and display the UI
        SwingUtilities.invokeLater(() -> {
            BankingUI bankingUI = new BankingUI(accounts);
            bankingUI.setVisible(true);
        });

        // -> symbol allows lambda expression, helps initiating swing correctly

    }
}
