package jce;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class Sample020GenDRBG {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
		byte[] rng;
		try{ 
			rng = genDrbg( "NativePRNGNonBlocking" ); 
		} catch ( NoSuchAlgorithmException e ) {
			System.out.println( "this server is not linux, while check create token.");
			rng = genDrbg( "Windows-PRNG" );
		}
		 
		System.out.println( DatatypeConverter.printBase64Binary( rng ) );
	}
	
	// NIST-90a Recommendation for Random Number Generation Using Deterministic	Random Bit Generators
	public static byte[] genDrbg( String algo ) throws NoSuchAlgorithmException {
		byte[] seed;
		SecureRandom sha1Random;
		byte[] sha1 = new byte[20];
		
		seed = SecureRandom.getInstance( algo ).generateSeed( 55 );
		
		sha1Random = SecureRandom.getInstance("SHA1PRNG");
		
		sha1Random.setSeed( seed ); 
		
		sha1Random.nextBytes( sha1 );
		
		return sha1;
	}
}
