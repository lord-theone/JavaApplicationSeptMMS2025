package banking;

public class CurrentAccount extends BankAccount{
 
    public CurrentAccount(String accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
    }
 
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
        } 
        else {
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from Current account " + accountNumber);
        }
    }
 
    @Override
    public double calculateInterest() {
        System.out.println("Current account " + accountNumber + " does not earn interest.");
        return 0.0;
    }
}
