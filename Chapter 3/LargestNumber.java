import java.util.Scanner; 

public class LargestNumber{
	public static void main(String[] args){
		int num1,i=2,max=0,num; 
		
		Scanner input = new Scanner(System.in); 
		
		
		System.out.print("Enter your [1] number: "); 
		num1 = input.nextInt();
		
		max = num1;
		while(i <= 9){
			
			System.out.printf("Enter your [%d] number: ",i); 
			num = input.nextInt();
			
			if(num > max){
				max = num;
			}
			i++;
		}
		System.out.printf("The maximum value is %d", max); 
		
	}
}