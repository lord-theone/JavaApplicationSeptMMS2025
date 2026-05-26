//QUESTION 2.34
import java.util.Scanner;

public class Population
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        double population;
        double growthRate;

        System.out.print("Enter current population: ");
        population = input.nextDouble();

        System.out.print("Enter annual growth rate: ");
        growthRate = input.nextDouble();

        for (int year = 1; year <= 5; year++)
        {
            population = population +
                (population * growthRate / 100);

            System.out.printf(
                "After %d year(s): %.2f%n",
                year, population);
        }
    }
}
