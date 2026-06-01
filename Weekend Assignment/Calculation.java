import java.util.Scanner;

public class Calculation {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        int number;
        int firstSum = 0;
        int secondSum = 0;
        int thirdSum = 0;

        
        for (int i = 1; i <= 10; i++) {

            System.out.print("Enter number " + i + ": ");
            number = input.nextInt();

            
            if (i == 1 || i == 5 || i == 10) {
                firstSum += number;
            }

            if (i == 3 || i == 8 || i == 2) {
                secondSum += number;
            }

            
            if (i == 4 || i == 7 || i == 6 || i == 9) {
                thirdSum += number;
            }
        }

        
        int product = firstSum * secondSum;

        int result = thirdSum - product;

        System.out.println("Final Result = " + result);

       
        if (result >= 100) {
            System.out.println("Hurray I did it");
        } else {
            System.out.println("I still need to learn more in Java");
        }

       
    }
}