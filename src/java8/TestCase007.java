package java8;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestCase007 {
	public static void main( String[] args ) {
		Random random = new Random();
		random.ints().limit(10).forEach( System.out::println);
		
		List<Integer> numbers = Arrays.asList( 3, 2, 2, 3, 7, 3, 5 );
		
		numbers.stream().map( i -> i*i ).distinct().forEach( System.out::println );;
		
		List<String> strings = Arrays.asList( "abc", "", "bc", "efg", "abcd" , "", "jkl" );
		long count = strings.stream().filter( string -> string.isEmpty() ).count();
		System.out.println(count);
		
		long count2 = strings.parallelStream().filter( string -> string.isEmpty() ).count();
		System.out.println( count2 );
		
		List<String> filtered = strings.stream().filter( string -> !string.isEmpty() ).collect( Collectors.toList() );
		System.out.println("filtered list : " + filtered);
		
		String mergedString = strings.stream().filter( string -> !string.isEmpty() ).collect( Collectors.joining(", " ) );
		System.out.println( "merged string : " + mergedString );
		
		IntSummaryStatistics stats = numbers.stream().mapToInt( (x) -> x).summaryStatistics();
		
		System.out.println( "highest number in list " + stats.getMax() );
		System.out.println( "lowest number in list " + stats.getMax() );
		System.out.println( "highest number in list " + stats.getMax() );
		System.out.println( "highest number in list " + stats.getMax() );
		
	}
}
