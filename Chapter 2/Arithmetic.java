import java.util.Scanner;
// QUESTION 2.15
public class Arithmetic
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int num1;
        int num2;

        System.out.print("Enter first integer: ");
        num1 = input.nextInt();

        System.out.print("Enter second integer: ");
        num2 = input.nextInt();

        System.out.printf("Sum = %d%n", (num1 + num2));
        System.out.printf("Product = %d%n", (num1 * num2));
        System.out.printf("Difference = %d%n", (num1 - num2));
        System.out.printf("Quotient = %d%n", (num1 / num2));
    }
}
