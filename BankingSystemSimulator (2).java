import java.util.*;

public class BankingSystemSimulator {
    
    static class Transaction {
        String type;
        double amount;

        Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
        }

        public String toString() {
            return type + ": $" + amount;
        }
    }

    static class Customer {
        int accountNumber;
        double balance;
        Stack<Transaction> history;

        Customer(int accountNumber) {
            this.accountNumber = accountNumber;
            this.balance = 0;
            this.history = new Stack<>();
        }

        void deposit(double amount) {
            balance += amount;
            history.push(new Transaction("Deposit", amount));
            System.out.println("Deposited $" + amount);
        }

        boolean withdraw(double amount) {
            if (balance >= amount) {
                balance -= amount;
                history.push(new Transaction("Withdraw", amount));
                System.out.println("Withdrew $" + amount);
                return true;
            } else {
                System.out.println("Insufficient balance.");
                return false;
            }
        }

        void checkBalance() {
            System.out.println("Current balance: $" + balance);
        }

        void showHistory() {
            if (history.isEmpty()) {
                System.out.println("No transactions available.");
            } else {
                System.out.println("Transaction History:");
                for (Transaction t : history) {
                    System.out.println(t);
                }
            }
        }

        void undoLastTransaction() {
            if (!history.isEmpty()) {
                Transaction last = history.pop();
                if (last.type.equals("Deposit")) {
                    balance -= last.amount;
                } else if (last.type.equals("Withdraw")) {
                    balance += last.amount;
                }
                System.out.println("Last transaction undone: " + last);
            } else {
                System.out.println("No transaction to undo.");
            }
        }
    }

    static HashMap<Integer, Customer> accounts = new HashMap<>();
    static Queue<Integer> serviceQueue = new LinkedList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Banking System Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. Add Customer to Queue");
            System.out.println("3. Process Next Customer");
            System.out.println("4. Exit");
            System.out.print("Enter option: ");
            int option = sc.nextInt();

            switch (option) {
                case 1 -> createAccount();
                case 2 -> enqueueCustomer();
                case 3 -> processNextCustomer();
                case 4 -> {
                    System.out.println("Exiting system.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void createAccount() {
        System.out.print("Enter new account number: ");
        int acc = sc.nextInt();
        if (accounts.containsKey(acc)) {
            System.out.println("Account already exists.");
        } else {
            accounts.put(acc, new Customer(acc));
            System.out.println("Account created successfully.");
        }
    }

    static void enqueueCustomer() {
        System.out.print("Enter account number to queue: ");
        int acc = sc.nextInt();
        if (accounts.containsKey(acc)) {
            serviceQueue.add(acc);
            System.out.println("Customer added to service queue.");
        } else {
            System.out.println("Account not found.");
        }
    }

    static void processNextCustomer() {
        if (serviceQueue.isEmpty()) {
            System.out.println("No customers in queue.");
            return;
        }

        int acc = serviceQueue.poll();
        Customer customer = accounts.get(acc);
        System.out.println("\n--- Serving Customer #" + acc + " ---");

        while (true) {
            System.out.println("\nChoose Operation:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Cash");
            System.out.println("3. Withdraw Cash");
            System.out.println("4. View Transaction History");
            System.out.println("5. Undo Last Transaction");
            System.out.println("6. Finish Session");
            System.out.print("Enter option: ");
            int op = sc.nextInt();

            switch (op) {
                case 1 -> customer.checkBalance();
                case 2 -> {
                    System.out.print("Enter amount to deposit: ");
                    double amt = sc.nextDouble();
                    customer.deposit(amt);
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double amt = sc.nextDouble();
                    customer.withdraw(amt);
                }
                case 4 -> customer.showHistory();
                case 5 -> customer.undoLastTransaction();
                case 6 -> {
                    System.out.println("Session ended for customer #" + acc);
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
