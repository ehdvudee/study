package jce.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class PemUtil {
	private static final Pattern KEY_PATTERN = Pattern.compile(
            "-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+" + // Header
                    "([a-z0-9+/=\\r\\n]+)" +                       // Base64 text
                    "-+END\\s+.*PRIVATE\\s+KEY[^-]*-+"           // Footer
    );
	
	private static final String RSA_PRI_START = "-----BEGIN RSA PRIVATE KEY-----\n";
	private static final String RSA_PRI_END = "\n-----END RSA PRIVATE KEY-----\n";
	private static final String CERT_START = "-----BEGIN CERTIFICATE-----\n";
	private static final String CERT_END = "\n-----END CERTIFICATE-----\n";
	
	public static void main( String[] args ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException {
		KeyStoreUtil keyStoreUtil = KeyStoreUtil.getInstance( "change".toCharArray(), "./jks/keystore.jks" );
		KeyPair kPair = keyStoreUtil.getKeyPair( "kms".toCharArray(), "kms" );
		Certificate cert = keyStoreUtil.getCertificate( "kms".toCharArray(), "kms" );
		
		PrivateKey pri = kPair.getPrivate();
		
		String pemK = pkcs8ToPem( pri );
		String pemC = certToPem( cert );
		
		PrintWriter pw1 = new PrintWriter("jks/pri.pem");
		PrintWriter pw2 = new PrintWriter("jks/cert.pem");
		
		pw1.write( pemK );
		pw2.write( pemC );
		
		pw1.close();
		pw2.close();
	}
	
	public static String pkcs8ToPem( PrivateKey pri ) {
		String b64Key = DatatypeConverter.printBase64Binary( pri.getEncoded() );
		String pemKey = new StringBuilder()
				.append( RSA_PRI_START )
				.append( slice64( b64Key ) )
				.append( RSA_PRI_END ).toString();
		
		System.out.println( pemKey );
		return pemKey;
	}
	
	public static String certToPem( Certificate cert ) throws CertificateEncodingException {
		String b64Cert = DatatypeConverter.printBase64Binary( cert.getEncoded() );
		String pemCert = new StringBuilder()
				.append( CERT_START )
				.append( slice64( b64Cert ) )
				.append( CERT_END ).toString();
		
		System.out.println( pemCert );
		
		return pemCert;
	}

	private static StringBuilder slice64( String str ) {
		StringBuilder sb = new StringBuilder();
		for ( int i=1; i<=str.length(); i++ ) {
			if ( (i % 64) != 0 )
				sb.append( str.charAt( i-1 ) );
			else {
				sb.append( str.charAt( i-1 ) );
				if ( !(i == str.length()) ) 
					sb.append("\n");
			}
		}
		
		return sb;
	}
	
}
