package java8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCase004 {
	public static void main( String[] args ) {
		List<String> names = new ArrayList<>();
		
		names.add( "Mahesh" );
		names.add( "suresh" );
		names.add( "ramesh" );
		names.add( "naresh" );
		names.add( "kalpesh" );
		Stream<String> stream1 = names.stream();
		stream1.forEach( System.out::println );
		
		
		Stream<String> stream2 = Stream.of("넷", "둘", "하나", "셋" );
		List<String> list = stream2.collect( Collectors.toList() );
		Iterator<String> itr = list.iterator();
		
		while ( itr.hasNext() ) {
			System.out.print( itr.next() + " ");
		}
	}
}
