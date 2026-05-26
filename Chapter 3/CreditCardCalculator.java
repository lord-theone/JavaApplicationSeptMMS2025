import java.util.Scanner;

public class CreditCardCalculator{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter your account number: "); 
		int account = scan.nextInt(); 
		
		System.out.print("Enter your balance at the beginning of the month: ");
		int beginingBalance = scan.nextInt();
		
		System.out.print("Total of all charged item: ");
		int chargedItem = scan.nextInt();	
		
		System.out.print("Total of all credit applied: ");
		int creditApplied = scan.nextInt();	
		
		System.out.print("Allowed credit limit: "); 
		int creditLimit = scan.nextInt();
		
		int newBalance = beginingBalance + chargedItem - creditApplied;
		
		boolean exceededCreditLimit = newBalance > creditLimit; 
		System.out.println(" ");
		System.out.println("=====================================================");
	
		System.out.println("The custumer profile is");
		System.out.println("=====================================================");
		
		System.out.printf("Account number: %d%n",account); 
		System.out.printf("Balance at the beginning of the month: %d%n",beginingBalance);
		System.out.printf("Total of all items charged: %d%n",chargedItem);
		System.out.printf("Credit Applied : %d%n",creditApplied);
		System.out.printf("Credit Limit: %d%n",creditLimit);
		System.out.printf("New Balance: %d%n",newBalance);
		System.out.printf("Credit Limit exceeded: %b%n",exceededCreditLimit);
		
		
	}
}