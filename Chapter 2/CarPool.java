//QUESTION 2.35 

import java.util.Scanner;

public class CarPool
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        double milesPerDay;
        double costPerGallon;
        double milesPerGallon;
        double parkingFees;
        double tolls;

        System.out.print("Miles driven per day: ");
        milesPerDay = input.nextDouble();

        System.out.print("Cost per gallon: ");
        costPerGallon = input.nextDouble();

        System.out.print("Miles per gallon: ");
        milesPerGallon = input.nextDouble();

        System.out.print("Parking fees per day: ");
        parkingFees = input.nextDouble();

        System.out.print("Tolls per day: ");
        tolls = input.nextDouble();

        double dailyCost =
            (milesPerDay / milesPerGallon)
            * costPerGallon
            + parkingFees + tolls;

        System.out.printf(
            "Daily driving cost = %.2f%n",
            dailyCost);
    }
}
