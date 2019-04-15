package jce.util;

public class DataTypeUtil {
	
	private DataTypeUtil() {
		throw new AssertionError();
	}
	
	public static int byteArrayIndexOf( byte[] to, byte[] from ) {
		int toIdx=0;
		int result=-1;
		
		for ( int fromIdx=0; fromIdx<from.length; fromIdx++ ) {
			if ( to[toIdx] == from[fromIdx] ) {
				
				if ( result == -1 )	result = fromIdx;
				if ( to.length == ++toIdx ) return result;
				
			} else {
				toIdx = 0;
				result = -1;
			}
		}
		
		return -1;
	}
	
	public static byte[] concatByteArrays(byte[]... bytes ) {
		int i = 0;
		
		for ( byte[] b : bytes ) {
			i = i + b.length;
		}
		
		byte[] concatedBytes = new byte[i];
		i = 0;
		
		for ( byte[] b : bytes ) {
			System.arraycopy( b, 0, concatedBytes, i, b.length );
			i = i + b.length;
		}
		
		return concatedBytes;
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
	
	public static String convertByteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
	
	
}
