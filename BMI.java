//QUESTION 2.33
import java.util.Scanner;

public class BMI
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int weight;
        int height;

        System.out.print("Enter weight in pounds: ");
        weight = input.nextInt();

        System.out.print("Enter height in inches: ");
        height = input.nextInt();

        :contentReference[oaicite:2]{index=2}

        int bmi = (703 * weight) / (height * height);

        System.out.printf("BMI = %d%n", bmi);

        System.out.println("BMI VALUES");
        System.out.println("Underweight: less than 18.5");
        System.out.println("Normal: between 18.5 and 24.9");
        System.out.println("Overweight: between 25 and 29.9");
        System.out.println("Obese: 30 or greater");
    }
}
