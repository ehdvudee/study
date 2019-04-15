package java8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sample001 {
	public static void main(String[] args) {
		// forEach() EXAMPLE
		setAndForEach();
	}
	
	public static void mapAndForEach() {
		Map <String, String> bookes = new HashMap<>();
		
		bookes.put("C_Lang", "shin");
		bookes.put("go_lang", "dong");
		bookes.put("jav_lang", "pyeong");
		
		bookes.forEach(( key, value ) -> System.out.println( "book name : " + key + ", author : " + value ));
		
		System.out.println();
		
		bookes.forEach(( key, value ) -> {
			System.out.println( "book name : " + key + "au tor: " + value );
		});
	}
	
	public static void listAndForEach() {
		List<String> color = new ArrayList<>();
		
		color.add("green");
		color.add("blue");
		color.add("black");
		
		color.forEach(( value ) -> System.out.println( "color : " + value ) );
		
		System.out.println();
		
		color.forEach(( val ) -> {
			System.out.println( "color : " + val );
		});
	}
	
	public static void setAndForEach() {
		Set<String> persons = new HashSet<>();
		
		persons.add("Yashwnat");
		persons.add("dinesh");
		persons.add("dong");
		
		persons.forEach(( value ) -> System.out.println( "value : " + value ) );
	
		System.out.println();
				
		persons.forEach(( value ) -> {
			System.out.println( "value : " + value );
		});
		
		System.out.println();
		
		persons.forEach( System.out::println );
	}
}
