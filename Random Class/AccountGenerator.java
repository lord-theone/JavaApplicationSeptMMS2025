import java.util.Random;

public class AccountGenerator {

    public static void main(String[] args) {
        String accountNumber = generateAccountNumber(10);
        System.out.println("Generated Account Number:    " + accountNumber);
    }

    
    public static String generateAccountNumber(int totalLength) {
        Random random = new Random();
        String digits = "0123456789";
        String accountNumber = "30";

        for (int i = 2; i < totalLength; i++) {
            accountNumber += digits.charAt(random.nextInt(digits.length()));
        }

        return accountNumber;
    }
}
