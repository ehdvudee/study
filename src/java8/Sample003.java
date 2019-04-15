package java8;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java8.Employee.Gender;

public class Sample003 {
	public static void main( String[] args ) throws ParseException {
		streamFilter();
	}
	
	public static void streamFilter() throws ParseException {
	
		DateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
		
		List<Employee> employees = new ArrayList<>();
		employees.add( new Employee( 11, "Yashwant Chavan",  dateFormat.parse( "01/08/1981" ), Gender.MALE ) );
		employees.add( new Employee( 2, "shin", dateFormat.parse( "11/07/1980"), Gender.MALE ) );
		employees.add( new Employee( 6, "name yoo!", dateFormat.parse( "02/08/1985"), Gender.MALE ) );
		employees.add( new Employee( 4, "Mash kr", dateFormat.parse( "01/22/1989" ), Gender.FEMALE ) );
		employees.add( new Employee( 7, "Mash kr", dateFormat.parse( "07/13/1982" ), Gender.FEMALE ) );
		
		System.out.println( "filter gender = FEMALE" );
		employees.stream().filter( e -> e.isFemale()).forEach(System.out::println);
		
		System.out.println( "\nFilter fender = male && employee id > 2 ");
		employees.stream().filter( e-> e.isMale() && e.getEmployeeId() > 2 ).forEach( System.out::println);
	
	}
}
