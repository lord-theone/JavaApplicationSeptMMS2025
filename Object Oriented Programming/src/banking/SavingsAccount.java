package banking;

public class SavingsAccount extends BankAccount{
    
     public SavingsAccount(String accountNumber, String accountHolder, double balance){
        super(accountNumber, accountHolder, balance);
    }

    @Override
    void withdraw(double amount) {
        if (amount <= 0){
            System.out.println("Withdrawal amount must be positive.");
        } else if (balance - amount < 500.0){
            System.out.println("Withdrawal denied: Savings account must maintain a minimum balance of $500.0");
        } else{
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from Savings account " + accountNumber);
        }
    }

    @Override
    double calculateInterest(){
        double interest = balance * 0.04;
        System.out.printf("Calculated interest for Savings account %s at 4.0%%: $%.2f%n", accountNumber, interest);
        return interest;
    }
}
