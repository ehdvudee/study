package test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Test2 {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
		List<byte[]> a = new ArrayList<byte[]>();
		
		int c = 8;
		int b = 18;
		System.out.println( b & c );
		
		MessageDigest md = MessageDigest.getInstance( "SHA-256" );
		
		byte[] test1 = md.digest( convertHexStringToByteArray( "79B2BED271B4C5459A0C3E1545C8C8CF" ) );
			
		System.out.println( convertByteArrayToHexString( test1 ).toUpperCase() );
	}
	
	public static String convertByteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
	
	
	public static byte[] convertHexStringToByteArray( String hexString ) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ( (Character.digit( hexString.charAt(i), 16 ) << 4 )
					+ Character.digit( hexString.charAt(i+1), 16 ) );
		}
		
		return data;
	}
}
