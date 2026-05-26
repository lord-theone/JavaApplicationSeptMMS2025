import java.util.Scanner;

public class FindTwoLargestNumbers {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        int number1;
        int largest;
        int secondLargest;

        System.out.print("Enter integer 1: ");
        number1 = input.nextInt();

        largest = number1;
		secondLargest = number1

        int counter = 2;

        while (counter <= 10) {

            System.out.printf("Enter integer %d: ", counter);
            int number = input.nextInt();

            if (number > largest) {
                secondLargest = largest;
                largest = number;
                
            }
            else if (number > secondLargest) {
					secondLargest = number; 
            }

            counter++;
        }

        System.out.printf("%nLargest number: %d%n", largest);
        System.out.printf("Second largest number: %d%n", secondLargest);

        input.close();
    }
}