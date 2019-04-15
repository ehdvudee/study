package nio.thread;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ReturnDigest extends Thread {
	private String filename;
	private byte[] digest;
	
	public ReturnDigest( String filename ) {
		this.filename = filename;
	}
	
	@Override public void run() {
		
		try {
			FileInputStream in = new FileInputStream( "./test/" + filename );
			MessageDigest sha = MessageDigest.getInstance( "SHA-256" );
			DigestInputStream din = new DigestInputStream( in, sha );
			
			while ( din.read() != -1 );
			din.close();
			digest = sha.digest();
		} catch ( IOException | NoSuchAlgorithmException e ) {
			e.printStackTrace();
		} 
	}
	
	public byte[] getDigest() {
		return digest;
	}
}
