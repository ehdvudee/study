package nio.thread;

import javax.xml.bind.DatatypeConverter;

public class ReturnDigestUserInterface {
	public static void main( String[] args ) {
		String[] strL = { "a1", "a2", "a3" };
		ReturnDigest[] dr = new ReturnDigest[strL.length];
		
		for ( int i=0; i<strL.length; i++ ) {
			dr[i] = new ReturnDigest( strL[i] );
			dr[i].start();
			
		}
		int j = 0;
		for ( int i=0; i<strL.length; i++ ) {
			while(true) {
				byte[] digest = dr[i].getDigest();
				if ( digest != null ) {
					StringBuilder result = new StringBuilder( strL[i] );
					result.append( " : " );
					result.append( DatatypeConverter.printHexBinary( digest ) );
					System.out.println( result );
					break;
				} else {
					System.out.println( i + " 번째 " + j++);
				}
			}
			
		}
	}
}
