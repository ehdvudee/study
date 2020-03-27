package http.pure.tls;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class InvokeTest {
	
	@Test
	public void test001() throws CertificateException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
		String b64Cert = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUQ4akNDQXRxZ0F3SUJBZ0lCRlRBTkJna3Foa2lHOXcwQkFRc0ZBREEyTVRRd01nWURWUVFERXl0TllYTjAKWlhJZ1QxVWdQU0JXYVdWM1pYSWdUeUE5SUVSeVpXRnRjMlZqZFhKcGRIa2dReUE5SUV0U01CNFhEVEU1TURZeApNVEF4TWpNeE1Wb1hEVEl3TURZeE1EQXhNak14TVZvd1dERldNRlFHQTFVRUF4Tk5kR1Z6ZEMxdFlXZHBZMnR0CmN5NWtjbVZoYlhObFkzVnlhWFI1TG1OdmJTQlBJRDBnZEdWemRDMXRZV2RwWTJ0dGN5Qk1JRDBnYzJWdmRXd2cKVXlBOUlITnZibWR3WVNCRElEMGdTMUl3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQgpBUUNDNUtlSE55KzNiazlyL2pTNUI4VkJCU0tXcHc1OVhIT2I0L0NrbThRYlViU21VaTNNYTRKU1R3RWpOTHV3CjVnL2pDYjdudTJQcnBoMitMK2VtaC9weE1iTjZTdU42OE9ZQmtTWVl6bEphTVVMR2FTRUpQSGk5Zm1uOXNOWWEKWDRmTmljUUR5cVZOY3BieTZHNVQzeVJ2Mi8xRUJnZWIzVTYzMG85bXlmSHVJa0hncGZPRi91ay9RRTJ2VVVFTApTNnBqU0txeXdDWXFtTkdVazByVndEbVZ5blhHYVJPaGZ1UXdQa2p1N1g4RGhSNVZ6Ykh3UHVIRGZLK2xiMWVxCitPMzdtQmg0TjN6eUZNKzJmMjcxRm81UzR0UTA4OUdGOXBQZHdYUG5ONCtyTkNGcHc4anA1ZnFCSjU4ZVVObG8KYkdYVis5a21qaldTYWJEUm11bTFkeSt0QWdNQkFBR2pnZWd3Z2VVd1hnWURWUjBqQkZjd1ZZQVU3NCtoekZHKwo0RG1oQzZrM1RjOHFvbFo4aGkyaE9xUTRNRFl4TkRBeUJnTlZCQU1USzAxaGMzUmxjaUJQVlNBOUlGWnBaWGRsCmNpQlBJRDBnUkhKbFlXMXpaV04xY21sMGVTQkRJRDBnUzFLQ0FRRXdDUVlEVlIwVEJBSXdBREFkQmdOVkhTVUUKRmpBVUJnZ3JCZ0VGQlFjREFRWUlLd1lCQlFVSEF3SXdEZ1lEVlIwUEFRSC9CQVFEQWdXZ01Db0dBMVVkRVFRagpNQ0dDSDNSbGMzUXRiV0ZuYVdOcmJYTXVaSEpsWVcxelpXTjFjbWwwZVM1amIyMHdIUVlEVlIwT0JCWUVGSnA4CldaL3V6a3VqZzZRKytSeXY4TFJ6RmtJME1BMEdDU3FHU0liM0RRRUJDd1VBQTRJQkFRQnJxUkEzVDV3QWloY2gKWUpBODRINk8vdUlmWi82V01GVXkzZVRRZFFWSXpFaitHYStZQXpjSG4vS0xMbTFGQ1dUVHgrYTlmbkRuVklFMgpzYVR0SFJZVFhoYTFkM2VZd2tXaXdhUGZoTm1BbDZyeGh5d3lPWWtTZGZuSk1yaUgzN3dMa3VPdlFPRTh3a0ZzCllyRnEySC8wUzI2MXV6a3FsL1VJdDRVQmMvNVlkOTdURWFFWnhXdFl3QVFXTm5YbWhYK0hCRitaTmU5c0Q2d2cKRHRGczZZRDhURFpLRTdPakhkWjdwWGoyQVNDUzhpaGdUNzJRNDRWQ3BMYkJTSytqSDMvRFhWVzlIeXI5Q2VuOQp5OEtBSktuZDRuMy9rR2RzT1NvbUxWWE1VT0dySko2UGhFOXN2aW9tVlRVTTMrcldRTzZhZXA4THV4bFQzQjhtCk1rQ0hKeVBrCi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
		
		CertificateFactory certFactory = CertificateFactory.getInstance( "X.509" );
		X509Certificate cert = (X509Certificate) certFactory.generateCertificate( new ByteArrayInputStream( DatatypeConverter.parseBase64Binary( b64Cert ) ) );
		
		// if usig TLSv1.1 in java 7 
//		System.setProperty("https.protocols", "TCLSv1,TLSv1.1,TLSv1.2");

		String host = "https://test-magickms.dreamsecurity.com";
		String url = "/test/home.do";
		SSLContext sc = getSSLContext( cert );
		URL u = new URL( host + url );
		
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		conn.setSSLSocketFactory( sc.getSocketFactory() );
		
		String ret = invokeKMS( conn, "GET", null );
		
		System.out.println( ret );
		
	}
	
	public SSLContext getSSLContext( X509Certificate cert ) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, KeyManagementException {
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance( keyStoreType );
		
		keyStore.load( null, null );
		keyStore.setCertificateEntry( "kmsCert", cert );
		
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance( tmfAlgorithm );
		tmf.init( keyStore );
		
		// 2019.06.11 - ehdvudee
		// common java se 6 is not avaiable TLSv1.1 and TLSv1.2
		// supported java se 6 is avaiable( TLSv1.1 : from u111, TLSv1.2 : from u121 )
		// Refer to BOUNCY CASTLE and Apache HTTP Client, to use TLSv1.1, TLSv1.2 in java se 6
		SSLContext sc = SSLContext.getInstance( "TLSv1.2" );
		sc.init( null, tmf.getTrustManagers(), null );
		
		return sc;
	}
	
	private String invokeKMS( HttpURLConnection conn, String httpMethod, JSONObject jObj ) throws IOException {

		OutputStream os = null;
		InputStream in = null;
		String result = null;

		try {
			conn.setRequestMethod( httpMethod );
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			if ( httpMethod.equals( "DELETE" ) ) {
				// We have to override the post method so that we send data, because under jdk 1.8 version always occur JDK-7157360 error
				conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
				conn.setRequestMethod("POST");
			}

			if ( jObj != null ) {
				conn.setDoInput(true);
				os = conn.getOutputStream();
				os.write(jObj.toString().getBytes());
			} 

			// read the response
			if ( conn.getResponseCode() == 200 /*OK*/ || conn.getResponseCode() ==201 /*Created*/ )
				in = new BufferedInputStream( conn.getInputStream() );
			else {
				in = new BufferedInputStream( conn.getErrorStream() );
			}
			
			result = IOUtils.toString(in, "UTF-8");

		} finally {
			if ( os != null ) try { os.close(); } catch ( IOException e ) {}
			if ( in != null) try { in.close(); } catch ( IOException e ) {}
			if ( conn != null ) try { conn.disconnect(); } catch ( Exception e ) {}	
		}

		return result;
	}
}
