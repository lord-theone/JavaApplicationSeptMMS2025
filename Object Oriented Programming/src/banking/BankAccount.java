package banking;


public abstract class BankAccount {
    String accountNumber;
    String accountHolder;
    double balance;
 
    public BankAccount(String accountNumber, String accountHolder, double balance){
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
 
    void deposit(double amount){
        if (amount <= 0){
            System.out.println("Deposit amount must be positive.");
            return;
        }
        balance += amount;
        System.out.println("Deposited $" + amount + " into account: " + accountNumber);
    }
 
    void displayBalance(){
        System.out.println("Account: " + accountNumber);
        System.out.println("Holder: " + accountHolder);
        System.out.printf("Balance: $%.2f%n", balance);
    }
 
    abstract void withdraw(double amount);
    abstract double calculateInterest();
}
