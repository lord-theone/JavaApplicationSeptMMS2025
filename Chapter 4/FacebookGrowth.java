public class FacebookGrowth {
    public static void main(String[] args) {
        double users = 1_000_000_000.0;  
        double growthRate = 0.04;        
        double target1 = 1_500_000_000.0;
        double target2 = 2_000_000_000.0; 

        int month = 0;
        int reachedTarget1 = -1;
        int reachedTarget2 = -1;

        while (reachedTarget2 == -1) {
            users *= (1 + growthRate);
            month++;

            if (reachedTarget1 == -1 && users >= target1)
                reachedTarget1 = month;

            if (reachedTarget2 == -1 && users >= target2)
                reachedTarget2 = month;
        }

        System.out.printf("Months to reach 1.5 billion users: %d%n", reachedTarget1);
        System.out.printf("Months to reach 2 billion users:   %d%n", reachedTarget2);
    }
}

