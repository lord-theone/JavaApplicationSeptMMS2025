public class OddProduct {
    public static void main(String[] args) {
        long product = 1;

        for (int i = 1; i <= 15; i += 2) {
            product *= i;
        }

        // Odd integers: 1,3,5,7,9,11,13,15
        System.out.printf("Product of odd integers 1-15: %d%n", product);
    }
}
