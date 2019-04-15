package java8;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sample004 {
	 public static void main( String[] args ) {
		 List<String> colors = Stream.of( "Red", "Green", "Blue", "Pink", "Whie", "Yellow", "Orange", "Purple" ).collect( Collectors.toList() );
		 Predicate<String> predicate = element -> element.startsWith( "P" );
		 
		 long count = colors.stream().filter( predicate ).count();
		 System.out.println("count with filter : " + count );
		 
		 count = colors.stream().count();
		 System.out.println("count without filter : " + count );
		 
	 }
}
