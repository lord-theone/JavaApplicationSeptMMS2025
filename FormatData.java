import java.util.Scanner;

public class FormatData{
	public static void main(String[] args){
		String name; 
		int age;
		String address;
		
		Scanner input = new Scanner(System.in);

		System.out.print("Enter your name: ");
		name = input.nextLine();
		
		System.out.print("Enter your address: ");
		address = input.nextLine(); 

		System.out.print("Enter your age: ");
		age = input.nextInt(); 
		
		
	
		
		System.out.printf("%s you are %d years old and your address is %s",name,age,address);
		
	}
}