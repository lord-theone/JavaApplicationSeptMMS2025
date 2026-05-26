import java.util.Scanner; 

public class GasMileage{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in); 
		int totalMiles = 0;
		int totalGallons = 0;
		
		while(true){
			
			
			
			System.out.print("Enter miles driven: ");
			int miles = scan.nextInt();
			
			if(miles == 0){
				
				System.out.println(" ");
				System.out.println("=====================================================");
			
				double averageMilesPerGalllon = (double)totalMiles/totalGallons;
				System.out.println("TotalMiles	TotalGallons	AverageOfMilesPerGallons");
				System.out.printf("%d		%d			%.2f",totalMiles,totalGallons,averageMilesPerGalllon);
				break;
			}
			
			System.out.print("Enter number of gallons used: ");
			int gallons = scan.nextInt();
			
			double milesPerGallon = (double) miles/gallons;
			
			totalMiles += miles;
			totalGallons += gallons;
			
			System.out.println(" ");
			System.out.println("=====================================================");
			
			System.out.println("	Miles	Gallons	Miles/Gallons");
			System.out.printf("	%d	%d	%.2f",miles,gallons,milesPerGallon);
			
			System.out.println(" ");
			System.out.println("=====================================================");
			
			
		}
	}
}