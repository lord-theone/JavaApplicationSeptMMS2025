import java.util.Scanner;
//QUESTION 2.16
public class Compare
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

        if (num1 > num2)
            System.out.printf("%d is larger%n", num1);

        if (num2 > num1)
            System.out.printf("%d is larger%n", num2);

        if (num1 == num2)
            System.out.println("These numbers are equal");
    }
}
