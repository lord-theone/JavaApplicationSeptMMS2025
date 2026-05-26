//Write java that when the user enter the day from 1 to 7 and the day of the week will be outputed 

import java.util.Scanner; 

public class DaysOfTheWeek{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in); 
		
		System.out.print("Enter a number form 1 to 7: ");
		int days = scan.nextInt();
		
		switch(days){
			
			case 1:
			System.out.println("Monday");
			break;
			
			case 2:
			System.out.println("Tuesday");
			break;
			
			case 3:
			System.out.println("Wednesday");
			break;
			
			case 4:
			System.out.println("Thursday");
			break;
			
			case 5:
			System.out.println("Friday");
			break;
			
			case 6:
			System.out.println("Saturday");
			break;
			
			case 7:
			System.out.println("Sunday");
			break;
			
			default: 
			
			System.out.print("The number you entered is invalid");
				
			
		}
	}
}