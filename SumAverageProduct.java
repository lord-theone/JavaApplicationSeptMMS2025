//Write a program to accept 5 numbers from a user and display the sum average and product of the 5 numbers 
// Write a program to accept a number from a user and check if the number is an even number dont use if 
//Write a java program to accept name age and address and format the data to give a meaningful meassage on the screen

import java.util.Scanner;

public class SumAverageProduct{
	public static void main(String[] args){
		
		int num1,num2,num3,num4,num5,sum,product,average;
		
		Scanner input = new Scanner(System.in); 
		
		System.out.print("Enter a number: ");
		num1 = input.nextInt(); 
		
		System.out.print("Enter a number: ");
		num2 = input.nextInt(); 
		
		System.out.print("Enter a number: ");
		num3 = input.nextInt(); 
		
		System.out.print("Enter a number: ");
		num4 = input.nextInt(); 
		
		System.out.print("Enter a number: ");
		num5 = input.nextInt(); 
		
		
		sum = num1+num2+num3+num4+num5;
		product = num1*num2*num3*num4*num5;
		average = (num1+num2+num3+num4+num5)/5;
		
		System.out.printf("The sum of the numbers is %d%n",sum); 
		System.out.printf("The product of the numbers is %d%n",product);
		System.out.printf("The average of the numbers is %d%n",average);
		
	}
} 