import java.util.Random;

public class PasswordGenerator {

    public static void main(String[] args) {
        String password = generatePassword(15);
        System.out.println("Generated 15-Digit Password: " + password);
    }

   
    public static String generatePassword(int length) {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[{]};:',<.>/?";
        String allChars = lowercase + uppercase + numbers + specialChars;

        Random random = new Random();
        String password = "";

       
        password += lowercase.charAt(random.nextInt(lowercase.length()));
        password += uppercase.charAt(random.nextInt(uppercase.length()));
        password += numbers.charAt(random.nextInt(numbers.length()));
        password += specialChars.charAt(random.nextInt(specialChars.length()));

       
        for (int i = 4; i < length; i++) {
            password += allChars.charAt(random.nextInt(allChars.length()));
        }

        String shuffledPassword = "";
        while (password.length() > 0) {
            int index = random.nextInt(password.length());
            shuffledPassword += password.charAt(index);
            password = password.substring(0, index) + password.substring(index + 1);
        }

        return shuffledPassword;
    }
}
