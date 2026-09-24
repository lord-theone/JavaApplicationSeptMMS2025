import java.util.Scanner;

public class FairTax {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] categories = {
            "Housing", "Food", "Clothing",
            "Transportation", "Education",
            "Health Care", "Vacations"
        };

        double totalExpenses = 0;

        for (String category : categories) {
            System.out.printf("Enter annual spending on %s: $", category);
            totalExpenses += scanner.nextDouble();
        }

        double fairTax23 = totalExpenses * 0.23;
        double fairTax30 = totalExpenses * 0.30;

        System.out.printf("%nTotal Annual Expenses: $%.2f%n", totalExpenses);
        System.out.printf("Estimated FairTax (23%%): $%.2f%n", fairTax23);
        System.out.printf("Estimated FairTax (30%%): $%.2f%n", fairTax30);

        scanner.close();
    }
}

