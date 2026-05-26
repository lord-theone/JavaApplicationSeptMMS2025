public class WorldPopulationGrowth {

    public static void main(String[] args) {

        long population = 8_100_000_000L; 
        double growthRate = 0.01; 
		
        long currentPopulation = population;
        long initialPopulation = population;

        System.out.printf("%s %s %s%n", "Year", "Population", "Increase");

        int yearDouble = -1;

        for (int year = 1; year <= 75; year++) {

            long newPopulation = (long) (currentPopulation * (1 + growthRate));
            long increase = newPopulation - currentPopulation;

            System.out.printf("%d %d %d%n",
                    year, newPopulation, increase);

            if (yearDouble == -1 && newPopulation >= 2 * initialPopulation) {
                yearDouble = year;
            }

            currentPopulation = newPopulation;
        }

        System.out.println();

        if (yearDouble != -1) {
            System.out.printf("Population doubles in year: %d%n", yearDouble);
        }
		else {
            System.out.println("Population does not double within 75 years.");
        }
    }
}