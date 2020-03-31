package http.apache;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class test {
	
	private String host = "http://localhost:8081";
	private byte[] oneCert = DatatypeConverter.parseBase64Binary( "MIIDczCCAlugAwIBAgIBCTANBgkqhkiG9w0BAQsFADBCMUAwPgYDVQQDDDfsl7DqtazqsJzrsJwy67aAMu2MgCBPVSA9IG1hZ2lja21zIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDU0NFoXDTIwMTIyMjA2MDU0NFowOjE4MDYGA1UEAxMvTyA9IERyZWFtLUtNUy1Db3JlIEwgPSBTZW91bCBTID0gU29uZy1QYSBDID0gS1IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCwnjlbJnB26XoqcF/XIwAiOz0Y55TDoJQ7aPLv7vFRWFJUAWFHQRJwSNA2RdJDyrYFihAEWkmRP6uKnrBcs9BTr9uCTabYJJ39R4DkqzMA48HqkehRmNlmJSfMeba7hXkp6MUFJfXF+/PJZAZZuoXXSGaQZG0G63xaYsYnzCdjB+8aCc1lMQOiuUY9hDBL210v+//O9jnnIPM3R8VHAkQ2+aViRJwJuJbqtZTkSo6tQ06WwCfm+C+nBVnmpXwyn54ZNuY+ZsDyXJNIhmVu4/qoXqfAKldMABG+mBeTGl2je8vCG30yL32YunRAdesydo+CtTfaGFfNbYEPAgox9Ao3AgMBAAGjfDB6MB8GA1UdIwQYMBaAFFEjESvibme8NY9wA4wxa51Vv+bQMAkGA1UdEwQCMAAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4EFgQUeDM5qKyPOeb0t/hDtZWEvGY3QfMwDQYJKoZIhvcNAQELBQADggEBABBoczg8Nq8/G8WA+ts4q/E6/+HJdcOr783QcmqnMST0LUzbzbdp0dbKI32TQfISkk31CSj7zbdz9ddHJC5LsO4QFJbYPtfQr0/4YxQOTsu6LBBXVShexv6gX7OBSEaIOLy+jPh5VGi3hxjX4TZINJZvHasJoFGn7fvufYbCjVgJwBcxnRIJ4a/cEOdLOCRVUW/UXSMTq6M5LS5yABGdlyH6yrJYxV7ZKEExU7nw7Sp78O+ku6Ao8KwTyIN3VhXT2qIV6egnIkwwAFltHq/kZliTz3odiP6CqmeYfpLv5JGReZ/M7wA+224/jTraCLYRmRMdn8cdfwnCnCDmeJQfPvU=" );

	@Test
	public void runGet() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet( host );
		
		HttpResponse httpResponse = httpClient.execute( httpGet );
		
		System.out.println( streamToString( httpResponse.getEntity().getContent() ) );
	}
	
	@Test
	public void runPost() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost( host );
		
		HttpResponse httpResponse = httpClient.execute( httpPost );
		
		System.out.println( streamToString( httpResponse.getEntity().getContent() ) );
	}
	
	@Test
	public void runWithResponseHandler() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet( host );
		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if ( status >= 200 && status <300 ) {
				HttpEntity entity = response.getEntity();
				if ( entity == null ) {
					return "";
				} else {
					return EntityUtils.toString( entity );
				}
			} else {
				return ""+status;
			}

		};
		
		String httpResponse = httpClient.execute( httpGet, responseHandler );
		System.out.println( httpResponse );
	}

	@Test
	public void runWithCloseConn() throws IOException {

		try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
			HttpGet httpGet = new HttpGet(host);
			try ( CloseableHttpResponse httpResponse = httpClient.execute(httpGet) ) {
				System.out.println(EntityUtils.toString(httpResponse.getEntity()));
			}
		}
 	}

 	@Test
	public void runWithAbort() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet( host );
		HttpResponse httpResponse = httpClient.execute( httpGet );

		System.out.println( httpResponse.getStatusLine() );
		httpGet.abort();
		System.out.println( httpResponse.getEntity().getContentLength() );

		HttpResponse httpResponse2 = httpClient.execute( httpGet );
		System.out.println(httpResponse2.getStatusLine() );
	}

	@Test
	public void runWithHttpReqInterceptor() throws IOException {
		HttpRequestInterceptor requestInterceptor = (httpRequest, httpContext) -> {
			System.out.println( " i'm going to run with interceptor" );
			for ( Header header : httpRequest.getAllHeaders() ) {
				System.out.println( header.getName() );
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().addInterceptorFirst( requestInterceptor ).build();
		HttpGet httpGet = new HttpGet( host );

		httpGet.setHeader( new BasicHeader( "test", "df" ) );
		System.out.println( httpClient.execute( httpGet ).getStatusLine() );
	}

	@Test
	public void runWithTLS() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContext oneSc = getSSLContext();
		SSLConnectionSocketFactory sslConnSocFactory = new SSLConnectionSocketFactory(oneSc, new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return false;
            }
        });

		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setSSLSocketFactory( sslConnSocFactory )
				.build();

		HttpRequestBase httpGet = new HttpGet( "https://10.10.30.115:8443" );

		HttpResponse httpResponse = httpClient.execute( httpGet );
		System.out.println( httpResponse.getStatusLine() );

	}

	private String streamToString( InputStream inStream ) throws IOException {
		StringBuilder textBuilder = new StringBuilder();
		try ( Reader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8 ) ) ) {
			int c;
			while ((c = reader.read()) != -1) {
				textBuilder.append((char) c);
			}
		}
		
		return textBuilder.toString();
	}

	private SSLContext getSSLContext() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, KeyManagementException {

		CertificateFactory certFactory = CertificateFactory.getInstance( "X.509" );
		X509Certificate kmsCertObj = (X509Certificate) certFactory.generateCertificate( new ByteArrayInputStream( oneCert ) );

		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance( keyStoreType );

		keyStore.load( null, null );
		keyStore.setCertificateEntry( "kmsCert", kmsCertObj );

		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance( tmfAlgorithm );

		tmf.init( keyStore );

		// 2019.06.11 - ehdvudee
		// common java se 6 is not avaiable TLSv1.1 and TLSv1.2
		// supported java se 6 is avaiable( TLSv1.1 : from u111, TLSv1.2 : from u121 )
		SSLContext sc = SSLContext.getInstance( "SSL" );
		sc.init( null, tmf.getTrustManagers(), null );

		HttpsURLConnection.setDefaultHostnameVerifier((var1, var2) -> true);

		return sc;
	}
}
