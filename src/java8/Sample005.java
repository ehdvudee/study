package java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Sample005 {
	public static void main( String[] args ) {
		
		List<String> fruits = Arrays.asList( "Apple", "Fig", "Banana", "Coconut" );
		List<String> vegetables = Arrays.asList( "Cabbage", "Capsicum", "Carrot", "Cauliflower" );
		String[] array = { "Apple", "Fig", "Banana", "Coconut" };
		Stream <String> fruitAndVegetables = Stream.concat( fruits.stream(), vegetables.stream() );
		
		// 스트림을 1번 이상 사용하기위해 사용한다.
		Supplier<Stream<String>> streamSupplier = () -> fruits.stream();
		
		streamSupplier.get().forEach( x -> System.out.println(x));
		
		long count = streamSupplier.get().filter( x -> "Apple".equals(x)).count();
		System.out.println( count );
		
		streamSupplier.get().forEach( x -> System.out.println(x + " ayo"));
		
		
//		System.out.println( fruitAndVegetables.count() );
//		fruitAndVegetables.forEach( e -> System.out.println( e ) );
		
		
	}
}
