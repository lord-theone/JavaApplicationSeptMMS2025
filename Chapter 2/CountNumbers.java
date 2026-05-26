//QUESTION 2.32
import java.util.Scanner;

public class CountNumbers
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int positive = 0;
        int negative = 0;
        int zero = 0;

        for (int i = 1; i <= 5; i++)
        {
            System.out.print("Enter number: ");
            int number = input.nextInt();

            if (number > 0)
                positive++;

            if (number < 0)
                negative++;

            if (number == 0)
                zero++;
        }

        System.out.printf("Positive: %d%n", positive);
        System.out.printf("Negative: %d%n", negative);
        System.out.printf("Zero: %d%n", zero);
    }
}
