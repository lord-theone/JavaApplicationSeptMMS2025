import java.util.Scanner;

public class SalesCommissionCalculator {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        double totalSales = 0.0;
        int item;

        System.out.println("Enter item numbers sold.");
        System.out.println("Enter -1 to finish.\n");

        while (true) {

            System.out.print("Item number: ");
            item = scan.nextInt();

            if (item == -1) {
                break;
            }

            switch (item) {

                case 1:
                    totalSales += 239.99;
                    break;

                case 2:
                    totalSales += 129.75;
                    break;

                case 3:
                    totalSales += 99.95;
                    break;

                case 4:
                    totalSales += 350.89;
                    break;

                default:
                    System.out.println("Invalid item number.");
            }
        }

        double earnings = 200 + ((9/100) * totalSales);

        System.out.printf("%nTotal Sales: $%.2f%n", totalSales);
        System.out.printf("Earnings: $%.2f%n", earnings);

        
    }
}