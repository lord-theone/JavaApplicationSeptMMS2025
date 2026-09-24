public class Factorials {
    public static void main(String[] args) {
        System.out.printf("%-5s%-25s%n", "n", "n!");
        System.out.println("-".repeat(30));

        long factorial = 1;
        for (int n = 1; n <= 20; n++) {
            factorial *= n;
            System.out.printf("%-5d%-25d%n", n, factorial);
        }
    }
}
