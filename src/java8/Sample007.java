package java8;

import java.util.Arrays;
import java.util.stream.Stream;

public class Sample007 {
	public static void main( String[] args ) {
		String[] fruits = { "Apple", "Fig", "Banana", "Coconut" };
		
		System.out.println("using Arrays.stream: = " );
		Stream<String> fruitsStream = Arrays.stream( fruits );
//		fruitsStream.forEach( System.out::println );
		fruitsStream.forEach( fruit -> System.out.println( fruit ) );
		
		System.out.println( "using stream.of(): -=");
		fruitsStream = Stream.of( fruits );
		fruitsStream.forEach( System.out::println );
		
		System.out.println(" primitive arrays using arrays.stream() : ");
		Integer[] numbers = { 11, 5, 18 };
		Stream<Integer> numberStream = Arrays.stream( numbers );
		numberStream.forEach( System.out::println ); 
	}
}
