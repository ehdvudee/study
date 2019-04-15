package java8;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Sample002 {
	public static void main( String[] args ) {
		
		try {
			sortingListofObject();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void sortingListOfString() {
		List<String> cities  = Arrays.asList( "Pune", "Mumbai", "Nashik", "Hyderabed", "delHa" );
		cities.stream().sorted().forEach( System.out::println );
		
		System.out.println( " \n reverse order \n ");
		cities.stream().sorted( Comparator.reverseOrder()).forEach( System.out::println );
	}
	
	public static void sortingListofObject() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		List<Employee> employees = new ArrayList<>();
		employees.add( new Employee( 11, "Yashwant Chavan",  dateFormat.parse( "01/08/1981" ), null ) );
		employees.add( new Employee( 2, "shin", dateFormat.parse( "11/07/1980"), null ) );
		employees.add( new Employee( 6, "name yoo!", dateFormat.parse( "02/08/1985"), null ) );
		employees.add( new Employee( 4, "Mash kr", dateFormat.parse( "01/22/1989" ), null ) );
		employees.add( new Employee( 7, "Mash kr", dateFormat.parse( "07/13/1982" ), null ) );
		
		System.out.println(" Before Sorting" );
		employees.forEach( ( e ) -> System.out.println( e ) );
		
		System.out.println( "\n Sort by Employee Id descending .. " );
		Comparator<Employee> byEmployeeId = ( e1, e2 ) -> Integer.compare( e2.getEmployeeId(), e1.getEmployeeId() );
		employees.stream().sorted(byEmployeeId).forEach( e -> System.out.println( e ) );
		
		System.out.println( "\n Sort by Employee Name .. " );
		Comparator<Employee> byName = ( e1, e2 ) -> e1.getName().compareTo( e2.getName() );
		employees.stream().sorted( byName ).forEach( e -> System.out.println( e ) );
		
		System.out.println( "\n Sort by Multiple key ( Name & Birth Date ).. ");
		Comparator<Employee> byBirthDate = ( e1, e2 ) -> e1.getBirthDate().compareTo( e2.getBirthDate() );
		
		employees.stream().sorted( byName.thenComparing( byBirthDate ) ).forEach( e -> System.out.println( e ));
	}
}
