package http.okhttp;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SSLContextUtils {
	
	private SSLContextUtils() {
		throw new AssertionError();
	}

    public static SSLContext getSSLContext( byte[] cert, String TlsAlgoName, final boolean chkHostName ) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, KeyManagementException {

        CertificateFactory certFactory = CertificateFactory.getInstance( "X.509" );
        X509Certificate kmsCertObj = (X509Certificate) certFactory.generateCertificate( new ByteArrayInputStream( cert ) );

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
        SSLContext sc = SSLContext.getInstance( TlsAlgoName );
        sc.init( null, tmf.getTrustManagers(), null );

        return sc;
    }
}
