package nio.thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class DigestThread extends Thread{
	private String filename;
	
	public DigestThread( String filename ) {
		this.filename = filename;
	}
	
	@Override
	public void run() {
		try {
			FileInputStream in = new FileInputStream( "./test/" + filename );
			MessageDigest sha = MessageDigest.getInstance( "SHA-256" );
			DigestInputStream din = new DigestInputStream( in, sha );
			while ( din.read() != -1  );
			din.close();
			
			byte[] digest = sha.digest();
			StringBuilder result = new StringBuilder( filename ) ;
			result.append( ": " );
			result.append( DatatypeConverter.printHexBinary( digest ) );
			System.out.println( result );
		} catch ( IOException | NoSuchAlgorithmException e ) {
			e.printStackTrace();
		} 
	}
	
	public static void main( String[] args ) {
		String[] strL = { "a1", "a2", "a3" };
		for ( String filename: strL ) {
			Thread t = new DigestThread( filename );
			t.start();
		}
	}
}
