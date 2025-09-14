public class SecureBankAccount {
    // Private fields (data hiding)
    private final String accountNumber; // read-only after creation
    private double balance;              // only modified through controlled methods
    private int pin;                    // write-only for security
    private boolean isLocked;           // internal security state
    private int failedAttempts;         // internal security counter

    // Private constants
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final double MIN_BALANCE = 0.0;

    // Constructor
    public SecureBankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance >= MIN_BALANCE ? initialBalance : 0.0;
        this.pin = 0;          // must be set later by user
        this.isLocked = false;
        this.failedAttempts = 0;
    }

    // Public getters for read-only or controlled access
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        if (isLocked) {
            System.out.println("Account is locked. Balance cannot be accessed.");
            return -1;  // indicate error
        }
        return balance;
    }

    public boolean isAccountLocked() {
        return isLocked;
    }

    // Security methods
    public boolean setPin(int oldPin, int newPin) {
        if (this.pin == 0 || validatePin(oldPin)) {
            this.pin = newPin;
            resetFailedAttempts();
            System.out.println("PIN successfully changed.");
            return true;
        } else {
            incrementFailedAttempts();
            System.out.println("Failed to change PIN: incorrect old PIN.");
            return false;
        }
    }

    public boolean validatePin(int enteredPin) {
        if (isLocked) {
            System.out.println("Account is locked. Access denied.");
            return false;
        }
        if (this.pin == enteredPin) {
            resetFailedAttempts();
            return true;
        } else {
            incrementFailedAttempts();
            System.out.println("Incorrect PIN.");
            return false;
        }
    }

    public boolean unlockAccount(int correctPin) {
        if (this.pin == correctPin) {
            isLocked = false;
            resetFailedAttempts();
            System.out.println("Account unlocked.");
            return true;
        } else {
            System.out.println("Incorrect PIN. Cannot unlock account.");
            return false;
        }
    }

    // Transaction methods
    public boolean deposit(double amount, int pin) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return false;
        }
        if (!validatePin(pin)) {
            System.out.println("Deposit failed due to invalid PIN.");
            return false;
        }
        if (isLocked) {
            System.out.println("Account locked. Cannot deposit.");
            return false;
        }
        balance += amount;
        System.out.printf("Deposited %.2f. New balance: %.2f\n", amount, balance);
        return true;
    }

    public boolean withdraw(double amount, int pin) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return false;
        }
        if (!validatePin(pin)) {
            System.out.println("Withdrawal failed due to invalid PIN.");
            return false;
        }
        if (isLocked) {
            System.out.println("Account locked. Cannot withdraw.");
            return false;
        }
        if (balance - amount < MIN_BALANCE) {
            System.out.println("Insufficient funds.");
            return false;
        }
        balance -= amount;
        System.out.printf("Withdrew %.2f. New balance: %.2f\n", amount, balance);
        return true;
    }

    public boolean transfer(SecureBankAccount target, double amount, int pin) {
        if (target == null) {
            System.out.println("Invalid target account.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }
        if (!validatePin(pin)) {
            System.out.println("Transfer failed due to invalid PIN.");
            return false;
        }
        if (isLocked) {
            System.out.println("Account locked. Cannot transfer.");
            return false;
        }
        if (balance - amount < MIN_BALANCE) {
            System.out.println("Insufficient funds to transfer.");
            return false;
        }

        // Withdraw from this account
        balance -= amount;
        // Deposit to target account (no PIN needed for target deposit)
        target.balance += amount;
        System.out.printf("Transferred %.2f to account %s. New balance: %.2f\n",
                          amount, target.getAccountNumber(), balance);
        return true;
    }

    // Private helper methods
    private void lockAccount() {
        isLocked = true;
        System.out.println("Account has been locked due to too many failed attempts.");
    }

    private void resetFailedAttempts() {
        failedAttempts = 0;
    }

    private void incrementFailedAttempts() {
        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            lockAccount();
        } else {
            System.out.printf("Failed attempts: %d\n", failedAttempts);
        }
    }

    // Main method for demo
    public static void main(String[] args) {
        SecureBankAccount acc1 = new SecureBankAccount("ACC123", 500.0);
        SecureBankAccount acc2 = new SecureBankAccount("ACC456", 1000.0);

        // Direct access to private fields - uncommenting below lines will cause compile errors:
        // acc1.balance = 10000;
        // System.out.println(acc1.pin);

        // Set PINs
        acc1.setPin(0, 1234);  // old PIN = 0 (initial), so should succeed
        acc2.setPin(0, 4321);

        System.out.println();

        // Successful deposit and withdraw
        acc1.deposit(200, 1234);
        acc1.withdraw(100, 1234);

        // Security: wrong PIN attempts
        System.out.println("\nTrying wrong PIN on acc1:");
        acc1.withdraw(50, 1111);  // 1st fail
        acc1.withdraw(50, 2222);  // 2nd fail
        acc1.withdraw(50, 3333);  // 3rd fail - should lock account

        // Trying to access balance after lock
        System.out.println("\nAccessing balance on locked account:");
        System.out.println("Balance: " + acc1.getBalance());

        // Try deposit on locked account
        acc1.deposit(50, 1234);

        // Unlock account with correct PIN
        System.out.println("\nUnlocking account:");
        acc1.unlockAccount(1234);

        // Withdraw after unlocking
        acc1.withdraw(50, 1234);

        // Transfer money between accounts
        System.out.println("\nTransferring money from acc2 to acc1:");
        acc2.transfer(acc1, 300, 4321);

        // Trying to withdraw more than balance
        System.out.println("\nTrying to withdraw more than balance from acc1:");
        acc1.withdraw(2000, 1234);
    }
}
