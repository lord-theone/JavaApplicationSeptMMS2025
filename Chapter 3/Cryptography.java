import java.util.Scanner;

public class Cryptography {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter a 4-digit number: ");
        int number = input.nextInt();

        int num1 = number / 1000;
        int num2 = (number / 100) % 10;
        int num3 = (number / 10) % 10;
        int num4 = number % 10;

        
        num1 = (num1 + 7) % 10;
        num2 = (num2 + 7) % 10;
        num3 = (num3 + 7) % 10;
        num4 = (num4 + 7) % 10;

        
        int temp;

        temp = num1;
        num1 = num3;
        num3 = temp;

        temp = num2;
        num2 = num4;
        num4 = temp;

        System.out.printf("Encrypted number: %d%d%d%d%n", num1, num2, num3, num4);

    }
}