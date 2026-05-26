//QUESTION 2.28 
import java.util.Scanner;

public class Circle
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int radius;

        System.out.print("Enter radius: ");
        radius = input.nextInt();

        System.out.printf("Diameter = %d%n", (2 * radius));

        
::contentReference[oaicite:0]{index=0}


        System.out.printf("Circumference = %f%n",
            (2 * Math.PI * radius));

        
::contentReference[oaicite:1]{index=1}


        System.out.printf("Area = %f%n",
            (Math.PI * radius * radius));
    }
}
