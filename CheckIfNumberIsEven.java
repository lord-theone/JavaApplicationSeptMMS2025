
import java.util.Scanner;

public class CheckIfNumberIsEven{
	public static void main(String[] args){
		
		int number;
		
		Scanner input = new Scanner(System.in); 
		
		System.out.print("Enter a number: "); 
		number = input.nextInt(); 
		
		boolean isEvenOrOdd = (number % 2 == 0);
		
		System.out.printf("Is the number even: %b", isEvenOrOdd);
		
		
	}
}