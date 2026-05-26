import java.util.Scanner;

public class SalaryCalculator {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        for (int employee = 1; employee <= 3; employee++) {

            System.out.printf("Employee %d%n", employee);

            System.out.print("Enter hours worked: ");
            int hoursWorked = input.nextInt();

            System.out.print("Enter hourly rate: ");
            double hourlyRate = input.nextDouble();

            double totalPay;

            if (hoursWorked <= 40) {
                totalPay = hoursWorked * hourlyRate;
            } 
			else {
                double overtimeHours = hoursWorked - 40;

                totalPay = (40 * hourlyRate) +
                           (overtimeHours * hourlyRate * 1.5);
            }

            System.out.printf("Gross pay: $%.2f%n%n", totalPay);
        }

     
    }
}