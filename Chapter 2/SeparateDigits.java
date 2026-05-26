//QUESTION 2.30
import java.util.Scanner;

public class SeparateDigits
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int number;

        System.out.print("Enter five-digit number: ");
        number = input.nextInt();

        int first = number / 10000;
        int second = (number % 10000) / 1000;
        int third = (number % 1000) / 100;
        int fourth = (number % 100) / 10;
        int fifth = number % 10;

        System.out.printf("%d   %d   %d   %d   %d%n",
            first, second, third, fourth, fifth);
    }
}
