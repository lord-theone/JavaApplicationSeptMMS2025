import java.time.Localdate;

public class UsingLocalDate{
	public static void main(String[] args){
		LocalDate todaysdate = LocalDate.now();
		LocalDate mybirthDate = LocalDate.of(2009,6,20;)
		LocalDate resumptionDate = LocalDate.parse("2028-10-15");
		
		boolean isleapYear = resumptionDate.isleapYear();
		boolean isEqual = resumptionDate.equals(myBirthDate);
		
		System.out.printf("Today's date is %s%n",todaysDate);
		System.out.printf("My birth date is %s%n",myBirthDate);
		System.out.printf("The resumption date is %s%n",resumptionDate);
		System.out.printf("The year of the resumption date is %s%n",resumptionDate.getYear());
		System.out.printf("The month of the resumption date is %s%n",resumptionDate.getMonth());
		System.out.printf("The day of the resumption date is %s%n",resumptionDate.getDayOfMonth());
		System.out.printf("The meeting date is %s%n",resumptionDate.plusDays(10));
		System.out.printf("The party date will be %s%n",resumptionDate.plusMonths(5));
		System.out.printf("Resumption date is a leap year? %b%n",isleapYear);
		System.out.printf("is %s the same as %s%n",resumptionDate,myBirthDate,isEqual);
	}
}