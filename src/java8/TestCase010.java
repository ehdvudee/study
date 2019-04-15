package java8;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TestCase010 {
	public static void main( String[] args) throws UnsupportedEncodingException { 
		String base64encodedString = Base64.getEncoder().encodeToString( "faihjephiwefpawef".getBytes("utf-8") );
		System.out.println( "base 64 encoded string basic : " + base64encodedString );
		
		
	}
}
