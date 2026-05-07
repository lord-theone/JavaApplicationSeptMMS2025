public class PrimitiveDataType{
	public static void main(String[] args){
		byte myAge = 30;
		System.out.printf("Miss Mercy is %d years old%n",myAge);
		
		short quantity = 20000;
		System.out.printf("The quantity of phones ordered is %,d%n",quantity);
		
		int nigeriaPopulation = 294848848;
		System.out.printf("The population of Nigeria is %,d%n",nigeriaPopulation);
		
		long worldPopulation = 47474778478474874L;
		System.out.printf("The World's population is %,d%n",worldPopulation);
		
		float price = 577858.84747F;
		System.out.printf("The price of each phone per unit is %,.2f%n",price);
		
		double myBalance = 736478747747474.8474747;
		System.out.printf("My account balance is %c%,.2f%n",'$',myBalance);
		
		char symbol = '%';
		System.out.printf("There is an increment in the world's population by 20.5%c%n",symbol);
		
		boolean isJavaFun = true;
		System.out.printf("Do you love java? %b",isJavaFun);
	}
}