package http.apache;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Apache HttpComponents(HttpClient) HttpClientFactory</h1>
 * refer to
 *  - https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
 *  - https://gist.github.com/arganzheng/dd53d1fff3c50d94cdd0
 *  - https://www.baeldung.com/httpclient-connection-management
 *  - https://stackoverflow.com/questions/19517538/ignoring-ssl-certificate-in-apache-httpclient-4-3/19950935#19950935
 *
 * @author      ehdvudee
 * @version     1.0.0
 * @since       2020-03-31
 */
public class HttpClientFactory {

    private static final HttpClientFactory instance = new HttpClientFactory();
    private final Map<String, HttpClient> httpClientMap = new HashMap<>();

    private final int maxTotalConnection = 400;
    private final int socketTimeOut = 30 * 1000;
    private final int connectionTimeOut = 5 * 1000;
    private final int keepAliave = 5 * 60 * 1000;

    private ConnectionKeepAliveStrategy keepAliavestrategy = (response, context) -> {
        HeaderElementIterator itr = new BasicHeaderElementIterator( response.headerIterator( HTTP.CONN_KEEP_ALIVE ) );

        while( itr.hasNext() ) {
            HeaderElement he = itr.nextElement();
            String param = he.getName();
            String value = he.getValue();

            if ( value != null && param.equalsIgnoreCase( "timeout" ) ) {
                return Long.parseLong(value) * 1000;
            }
        }

        return keepAliave;
    };

    private HttpClientFactory() {}

    public static HttpClientFactory getInstance() {
        return instance;
    }

    public HttpClient getHttpClient(String host, int port ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        return getHttpClient( host, port, null );
    }

    public HttpClient getHttpClient( String host, int port, byte[] cert ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        return getHttpClient( host, port, cert, false );
    }

    public HttpClient getHttpClient( String host, int port, byte[] cert, boolean hostNameChk ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return getHttpClient( host, port, cert, hostNameChk, EnumTLS.SSL );
    }

    public HttpClient getHttpClient( String host, int port, byte[] cert, boolean hostNameChk, EnumTLS enumTls ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        String key = new StringBuilder( host ).append( port ).toString();

        HttpClient httpClient = httpClientMap.get( key );
        if ( httpClient == null ) {
            synchronized ( HttpClientFactory.class ) {
                if ( httpClientMap.get( key ) == null ) {
                    createHttpClient( key, host, cert, hostNameChk, enumTls );
                }
                httpClient = httpClientMap.get( key );
            }
        }

        return httpClient;
    }

    private void createHttpClient(String key, String host, byte[] cert, boolean hostNameChk, EnumTLS enumTls ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        PoolingHttpClientConnectionManager connManager;

        if ( host.contains( "https://" ) && cert != null ) {
            SSLContext sc = getSSLContext( cert, enumTls );
            SSLConnectionSocketFactory sslConnSocFactory = new SSLConnectionSocketFactory( sc, (s, sslSession) -> !hostNameChk );

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register( "https", sslConnSocFactory )
                    .build();

            connManager = new PoolingHttpClientConnectionManager( socketFactoryRegistry );
        } else {
            connManager = new PoolingHttpClientConnectionManager();
        }

        connManager.setMaxTotal( maxTotalConnection );
        connManager.setDefaultMaxPerRoute( maxTotalConnection );

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout( socketTimeOut )
                .setConnectTimeout( connectionTimeOut )
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setKeepAliveStrategy( keepAliavestrategy )
                .setConnectionManager( connManager )
                .setDefaultRequestConfig( requestConfig )
                .build();

        httpClientMap.put( key, httpClient );

        IdleConnectionMonitorThread staleMonitor = new IdleConnectionMonitorThread( connManager );
        staleMonitor.start();
    }

    private class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connManager;
        private volatile boolean shutdown;

        private IdleConnectionMonitorThread( PoolingHttpClientConnectionManager connManager ) {
            super();
            this.connManager = connManager;
        }

        @Override
        public void run() {
            try {
                while ( !shutdown ) {
                    synchronized ( this ) {
                        wait( 10000 );
                        connManager.closeExpiredConnections();
                        connManager.closeIdleConnections( 30, TimeUnit.SECONDS );
                    }
                }
            } catch ( InterruptedException e ) {
                shutdown();

            }
        }

        private void shutdown() {
            shutdown = true;
            synchronized ( this ) {
                notifyAll();
            }
        }
    }

    private SSLContext getSSLContext( byte[] cert, EnumTLS enumTls ) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, KeyManagementException {

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
        SSLContext sc = SSLContext.getInstance( enumTls.getProtocol() );
        sc.init( null, tmf.getTrustManagers(), null );

        HttpsURLConnection.setDefaultHostnameVerifier((var1, var2) -> true);

        return sc;
    }
}
