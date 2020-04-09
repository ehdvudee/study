package http.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpFactory {

    private static final OkHttpFactory instance = new OkHttpFactory();
    private final Map<String, OkHttpClient> clientMap = new HashMap<String, OkHttpClient>();

    private final int maxIdleConnections = 20;
    private final int keepAlive = 5 * 60;
    private final long socketTimeOut = 30;
    private final long connectionTimeOut = 10;

    private OkHttpFactory() {}

    public static OkHttpFactory getInstance() {
        return instance;
    }

    public OkHttpClient getHttpClient( String host ) {
        return this.getHttpClient( host, null );
    }

    public OkHttpClient getHttpClient( String host, byte[] cert ) {
        return this.getHttpClient( host, cert, false );
    }

    public OkHttpClient getHttpClient( String host, byte[] cert, boolean hostNameChk ) {
        return this.getHttpClient( host, cert, hostNameChk, "SSL" );
    }

    public  OkHttpClient getHttpClient( String host, byte[] cert, boolean hostNameChk, String sslVersion ) {
        OkHttpClient client = clientMap.get( host );
        if ( client == null ) {
            synchronized ( this ) {
                if ( clientMap.get( host ) == null ) {
                    createOkHttpClient( host, cert, hostNameChk, sslVersion );
                }
                client = clientMap.get ( host );
            }
        }

        return client;
    }

    private void createOkHttpClient( String host, byte[] cert , final boolean hostNameChk, String sslVersion ) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if ( host.contains( "https://" ) && cert != null ) {
            SSLContext sslCtx;
            HostnameVerifier hostNameVerifier;

            try {
                sslCtx = SSLContextUtils.getSSLContext( cert, sslVersion, false );
            } catch ( Exception e ) {
                throw new RuntimeException( e );
            }

            hostNameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify( String s, SSLSession sslSession ) {
                    return !hostNameChk;
                }
            };

            clientBuilder.sslSocketFactory( sslCtx.getSocketFactory() )
                    .hostnameVerifier( hostNameVerifier );
        }

        ConnectionPool connPool = new ConnectionPool( maxIdleConnections, keepAlive, TimeUnit.SECONDS );

        OkHttpClient client = clientBuilder.readTimeout( socketTimeOut, TimeUnit.SECONDS )
                .connectTimeout( connectionTimeOut, TimeUnit.SECONDS )
                .connectionPool( connPool )
                .build();

        clientMap.put( host, client );
    }
}
