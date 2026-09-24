import java.util.Scanner;

public class SmallestValue {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many values? ");
        int count = scanner.nextInt();

        int smallest = Integer.MAX_VALUE;

        for (int i = 1; i <= count; i++) {
            System.out.printf("Enter integer %d: ", i);
            int value = scanner.nextInt();
            if (value < smallest)
                smallest = value;
        }

        System.out.printf("Smallest value: %d%n", smallest);
        scanner.close();
    }
}
