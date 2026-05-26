//QUESTION 2.25 
import java.util.Scanner;

public class OddEven
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int number;

        System.out.print("Enter integer: ");
        number = input.nextInt();

        if (number % 2 == 0)
            System.out.println("Even");

        if (number % 2 != 0)
            System.out.println("Odd");
    }
}
