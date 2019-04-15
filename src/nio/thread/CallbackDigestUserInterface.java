package nio.thread;

import javax.xml.bind.DatatypeConverter;

public class CallbackDigestUserInterface {
	public static void receiveDigest( byte[] digest, String name ) {
		StringBuilder result = new StringBuilder( name );
		result.append( "; " );
		result.append(DatatypeConverter.printHexBinary( digest ) );
		System.out.println( result );
	}
	
	public static void main( String[] args ) {
		String[] strL = { "a1", "a2", "a3" };
		for ( String filename : strL ) {
			CallbackDigest cb = new CallbackDigest( filename );
			Thread t = new Thread ( cb );
			t.start();
		}
	}
}
