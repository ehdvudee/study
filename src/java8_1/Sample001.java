package java8_1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Sample001 {
	
	public static void main( String[] args ) {
		
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 200, Dish.Type.MEAT),
				new Dish("beef", false, 300, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french", true, 500, Dish.Type.OTHER),
				new Dish("rice", true, 500, Dish.Type.OTHER),
				new Dish("season fruit", true, 500, Dish.Type.OTHER),
				new Dish("pizza", true, 500, Dish.Type.OTHER),
				new Dish("prawns", true, 500, Dish.Type.FISH),
				new Dish("salmon", true, 600, Dish.Type.FISH)
				);
		
		List<String> threeHighCaloricDishNames =
				menu.stream()
				.filter( dish -> dish.getCalories() > 300 )
				.map(Dish::getName)
				.limit(3)
				.collect( Collectors.toList());
		
		threeHighCaloricDishNames.stream().forEach(( d) -> System.out.println( d) );
	}
}
