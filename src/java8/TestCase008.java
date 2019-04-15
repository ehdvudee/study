package java8;

import java.util.Optional;

public class TestCase008 {
	
	public static void main( String args[] ) {
		TestCase008 tester = new TestCase008();
		Integer value1 = null;
		Integer value2 = new Integer(10);
		
		//Optional.ofNullable - allows passed parameter to be null.
		Optional<Integer> a = Optional.ofNullable( value1 );
		
		//Optional.of - thorws NullpointerException if passed parameter is null
		Optional<Integer> b = Optional.of( value2 );
		
		System.out.println( tester.sum(a, b) );
	}
	
	public Integer sum(Optional<Integer> a, Optional<Integer> b ) {
		
		System.out.println( "First parameter is present: " + a.isPresent() );
		System.out.println( "seconed parameter is present: " + b.isPresent() );
		
		Integer value1 = a.orElse( new Integer(0) );
		
		Integer value2 = b.get();
		return value1 + value2;
		
	}
}
