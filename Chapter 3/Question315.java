public class Question315{
	public static void main(String[] args){
		int age = 70,x=1,total=0,y=1;
		
		if(age >=65){
			System.out.println("Age is greater than or equal to 65");
			
		}
		else{
			System.out.println("Age is less than 65"); 
		}
		
		System.out.println(" ");
		System.out.println("============================================");
		
		while(x<=10){
			total += x;
			x++;
			System.out.println("total= "+total);
		}
		System.out.println(" ");
		System.out.println("============================================");
		
		while (x<=100){
			total += x; 
			x++; 
			System.out.println("total= "+total);
		}
		System.out.println(" ");
		System.out.println("============================================");
		while(y>0){
			System.out.println(y); 
			++y;
		}
	}
		
}