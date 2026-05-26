//QUESTION 2.26 
import java.util.Scanner;

public class Multiples
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

        if (num1 % num2 == 0)
            System.out.println("First is a multiple of second");

        if (num1 % num2 != 0)
            System.out.println("First is not a multiple of second");
    }
}
