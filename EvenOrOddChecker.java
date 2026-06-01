import java.util.Scanner;

public class EvenOrOddChecker{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		char option;
		
		do{
			System.out.print("Enter any number: ");
			int num = scan.nextInt();
			
			if(num % 2 == 0){
				System.out.println("This is an even number");
				System.out.println("");
				System.out.println("==================================================================");
			}
			else{
				System.out.println("This is an odd number");
				System.out.println("");
				System.out.println("==================================================================");
			}
			
			System.out.printf("Do you want to run the program again(Y/N): ");
			option = scan.next().charAt(0);
		}while(option == 'Y' || option == 'y');
		System.out.println("");
		System.out.println("==================================================================");
		System.out.printf("GoodBye.................");
	}
}	