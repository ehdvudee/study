package java8;

public class TestCase003 {
	
	final static String salutation = "Hello! ";
	
	public static void main(String[] args) {
		GreetingService greetService1 = message -> System.out.println( salutation + message );
		greetService1.sayMessage( "TPTPTPTPTP" );
	}
	
	interface GreetingService {
		void sayMessage( String message );
	}
}
