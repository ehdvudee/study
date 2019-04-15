package test;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import jce.util.DataTypeUtil;

public class test3 {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
		long currentTime = new Date().getTime();
		byte[] srNum = new byte[20];
		String id = "ehdvudee";
		
		SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG" );
		sr.nextBytes( srNum );
		
		MessageDigest md = MessageDigest.getInstance( "SHA-256" );
		md.update( srNum );
		md.update( longToBytes( currentTime ) );
		md.update( id.getBytes() );
		System.out.println( ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong( currentTime ).array() );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( srNum ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( longToBytes( currentTime ) ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( id.getBytes() ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( md.digest() ) );
	}
	
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}
}
