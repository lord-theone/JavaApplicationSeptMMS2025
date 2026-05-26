//QUESTION 2.24 
import java.util.Scanner;

public class LargestSmallest
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int number;
        int largest;
        int smallest;

        System.out.print("Enter integer: ");
        number = input.nextInt();

        largest = number;
        smallest = number;

        for (int i = 1; i < 5; i++)
        {
            System.out.print("Enter integer: ");
            number = input.nextInt();

            if (number > largest)
                largest = number;

            if (number < smallest)
                smallest = number;
        }

        System.out.printf("Largest: %d%n", largest);
        System.out.printf("Smallest: %d%n", smallest);
    }
}
