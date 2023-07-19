import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class User {
    private String name;
    private String address;
    private String contact;
    private double balance;
    private String accountNumber;

    public User(String name, String address, String contact, double balance, String accountNumber) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void updateBalance(double amount) {
        balance += amount;
    }
}

class Transaction {
    private LocalDateTime dateTime;
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private double amount;

    public Transaction(LocalDateTime dateTime, String senderAccountNumber, String recipientAccountNumber, double amount) {
        this.dateTime = dateTime;
        this.senderAccountNumber = senderAccountNumber;
        this.recipientAccountNumber = recipientAccountNumber;
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public double getAmount() {
        return amount;
    }
}

class BankingSystem {
    private List<User> users;
    private List<Transaction> transactions;
    private Scanner scanner;

    public BankingSystem() {
        users = new ArrayList<>();
        transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void registerUser() {
        System.out.println("User Registration");
        System.out.println("------------------");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your address: ");
        String address = scanner.nextLine();

        System.out.print("Enter your contact number: ");
        String contact = scanner.nextLine();

        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        String accountNumber = generateAccountNumber();

        User user = new User(name, address, contact, initialDeposit, accountNumber);
        users.add(user);

        System.out.println("\nRegistration Successful!");
        System.out.println("Account Number: " + accountNumber);
    }

    private String generateAccountNumber() {
        // Generate a random 6-digit account number
        int accountNumber = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(accountNumber);
    }

    public void manageAccount() {
        System.out.println("\nAccount Management");
        System.out.println("------------------");

        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUserByAccountNumber(accountNumber);
        if (user != null) {
            System.out.println("\nAccount Information");
            System.out.println("-------------------");
            System.out.println("Name: " + user.getName());
            System.out.println("Address: " + user.getAddress());
            System.out.println("Contact: " + user.getContact());
            System.out.println("Account Balance: " + user.getBalance());
        } else {
            System.out.println("\nAccount not found!");
        }
    }

    public void deposit() {
        System.out.println("\nDeposit");
        System.out.println("-------");

        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUserByAccountNumber(accountNumber);
        if (user != null) {
            System.out.print("Enter the deposit amount: ");
            double depositAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            user.updateBalance(depositAmount);

            System.out.println("\nDeposit Successful!");
            System.out.println("Transaction Amount: " + depositAmount);
            System.out.println("Updated Balance: " + user.getBalance());

            Transaction transaction = new Transaction(LocalDateTime.now(), accountNumber, "", depositAmount);
            transactions.add(transaction);
        } else {
            System.out.println("\nAccount not found!");
        }
    }

    public void withdraw() {
        System.out.println("\nWithdrawal");
        System.out.println("----------");

        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUserByAccountNumber(accountNumber);
        if (user != null) {
            System.out.print("Enter the withdrawal amount: ");
            double withdrawalAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            if (withdrawalAmount <= user.getBalance()) {
                user.updateBalance(-withdrawalAmount);

                System.out.println("\nWithdrawal Successful!");
                System.out.println("Transaction Amount: " + withdrawalAmount);
                System.out.println("Updated Balance: " + user.getBalance());

                Transaction transaction = new Transaction(LocalDateTime.now(), accountNumber, "", -withdrawalAmount);
                transactions.add(transaction);
            } else {
                System.out.println("\nInsufficient funds!");
            }
        } else {
            System.out.println("\nAccount not found!");
        }
    }

    public void transferFunds() {
        System.out.println("\nFund Transfer");
        System.out.println("-------------");

        System.out.print("Enter your account number: ");
        String senderAccountNumber = scanner.nextLine();

        User sender = findUserByAccountNumber(senderAccountNumber);
        if (sender != null) {
            System.out.print("Enter the recipient's account number: ");
            String recipientAccountNumber = scanner.nextLine();

            User recipient = findUserByAccountNumber(recipientAccountNumber);
            if (recipient != null) {
                System.out.print("Enter the transfer amount: ");
                double transferAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character

                if (transferAmount <= sender.getBalance()) {
                    sender.updateBalance(-transferAmount);
                    recipient.updateBalance(transferAmount);

                    System.out.println("\nTransfer Successful!");
                    System.out.println("Transferred Amount: " + transferAmount);
                    System.out.println("Sender's Updated Balance: " + sender.getBalance());
                    System.out.println("Recipient's Updated Balance: " + recipient.getBalance());

                    Transaction transaction = new Transaction(LocalDateTime.now(), senderAccountNumber, recipientAccountNumber, transferAmount);
                    transactions.add(transaction);
                } else {
                    System.out.println("\nInsufficient funds!");
                }
            } else {
                System.out.println("\nRecipient's account not found!");
            }
        } else {
            System.out.println("\nAccount not found!");
        }
    }

    public void displayAccountStatement() {
        System.out.println("\nAccount Statement");
        System.out.println("-----------------");

        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        User user = findUserByAccountNumber(accountNumber);
        if (user != null) {
            System.out.println("\nTransaction History");
            System.out.println("-------------------");
            System.out.println("Date\t\t\t\t\tSender/Recipient Account Number\t\tTransaction Amount\t\tBalance");
            System.out.println("-------------------------------------------------------------------------------");

            for (Transaction transaction : transactions) {
                if (transaction.getSenderAccountNumber().equals(accountNumber) || transaction.getRecipientAccountNumber().equals(accountNumber)) {
                    String transactionType = transaction.getSenderAccountNumber().equals(accountNumber) ? "Sent" : "Received";
                    String otherAccountNumber = transaction.getSenderAccountNumber().equals(accountNumber) ? transaction.getRecipientAccountNumber() : transaction.getSenderAccountNumber();
                    System.out.println(transaction.getDateTime() + "\t" + otherAccountNumber + "\t\t\t\t" + transaction.getAmount() + "\t\t\t" + user.getBalance());
                }
            }
        } else {
            System.out.println("\nAccount not found!");
        }
    }

    private User findUserByAccountNumber(String accountNumber) {
        for (User user : users) {
            if (user.getAccountNumber().equals(accountNumber)) {
                return user;
            }
        }
        return null;
    }
}

public class BankingInformationSystem {
    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nBanking Information System");
            System.out.println("--------------------------");
            System.out.println("1. User Registration");
            System.out.println("2. Account Management");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Fund Transfer");
            System.out.println("6. Account Statement");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    bankingSystem.registerUser();
                    break;
                case 2:
                    bankingSystem.manageAccount();
                    break;
                case 3:
                    bankingSystem.deposit();
                    break;
                case 4:
                    bankingSystem.withdraw();
                    break;
                case 5:
                    bankingSystem.transferFunds();
                    break;
                case 6:
                    bankingSystem.displayAccountStatement();
                    break;
                case 0:
                    System.out.println("\nExiting the program... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }
}

