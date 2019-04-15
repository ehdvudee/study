package java8;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Sample008 {
	public static void main( String[] args ) {
		
		String[] fruits = {"Apple", "Fig", "Banana", "Coconut" };
		Stream<String> fruitsStream = Arrays.stream( fruits );
		String[] fruitsArray = fruitsStream.toArray( String[]::new );
		System.out.println( " String array : " + Arrays.toString( fruitsArray ) );
			
		int[] intArray = { 1, 2, 3};
		intArray = IntStream.of( intArray ).toArray();
		System.out.println( "int array : " + Arrays.toString( intArray ) );
		
		long[] longArray = { 11, 21, 31 };
		longArray = LongStream.of( longArray ).toArray();
		System.out.println( "long array : " + Arrays.toString( longArray ) );
		
		double[] doubleArray = {1.1, 2.1, 3.1 };
		doubleArray = DoubleStream.of( doubleArray ).toArray();
		System.out.println( "double array : " + Arrays.toString( doubleArray ) );
	}
}
