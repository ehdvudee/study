package java8;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCase001 {
	public static void main( String[] args ) {
		List<String> stringList = Stream.of( "a", "b", "c", "allo" ).collect(Collectors.toList() );
	
		for ( String a : stringList ) {
			System.out.println(a);
		}
		
		stringList.forEach((value) -> System.out.println( value));
	}
}
