package java8;

public interface Vehicle {
	
	String a="d";
	
	default void print() {
		System.out.println( "i am a vehicle! ");
	}
	
	static void blowHorn() {
		System.out.println( "Blowing horn !!!" );
	}
}


