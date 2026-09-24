package banking;

public class BankingSystem {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("SAV001", "Alice Johnson", 2000.0);
        CurrentAccount current = new CurrentAccount("CUR001", "Bob Smith", 1500.0);
 
        System.out.println("===== Initial Balances =====");
        savings.displayBalance();
        current.displayBalance();
 
        System.out.println("\n===== Deposit Operations =====");
        savings.deposit(500.0);
        current.deposit(300.0);
 
        System.out.println("\n===== Withdrawal Operations =====");
        savings.withdraw(1000.0);
        savings.withdraw(2000.0);
 
        current.withdraw(2500.0);
        current.withdraw(500.0);
 
        System.out.println("\n===== Balances After Transactions =====");
        savings.displayBalance();
        current.displayBalance();
 
        System.out.println("\n===== Interest Calculation =====");
        savings.calculateInterest();
        current.calculateInterest();
    }

}
