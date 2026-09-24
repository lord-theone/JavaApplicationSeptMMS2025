public class CompoundInterest {
    public static void main(String[] args) {
        double principal = 1000.00;

        for (int rate = 5; rate <= 10; rate++) {
            double interestRate = rate / 100.0;
            System.out.printf("%nInterest Rate: %d%%%n", rate);
            System.out.printf("%-10s%-15s%n", "Year", "Amount");
            System.out.println("-".repeat(25));

            for (int year = 1; year <= 10; year++) {
                double amount = principal * Math.pow(1 + interestRate, year);
                System.out.printf("%-10d$%-14.2f%n", year, amount);
            }
        }
    }
}

