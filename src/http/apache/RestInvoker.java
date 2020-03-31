package http.apache;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

public class RestInvoker implements IInvoker {

    private final String host;
    private final int port;
    private final HttpClient httpClient;

    public RestInvoker( String host, int port, HttpClient httpClient ) {
        this.host = host;
        this.port = port;
        this.httpClient = httpClient;
    }

    @Override
    public String invokeHttp(String target, Map<String, String> headers, String httpMethod, String body) {
        try {
            HttpRequestBase httpReq = getHttpUriRequest( host, port, httpMethod, target);

            setDefaultHeaders( httpReq );
            if ( headers != null ) {
                for ( String key : headers.keySet() ) {
                    httpReq.setHeader( key, headers.get( key ) );
                }
            }

            if ( !(httpReq instanceof HttpGet) && body != null )
                ( (HttpEntityEnclosingRequestBase) httpReq ).setEntity( new StringEntity( body /*JSON*/ ) );

            return EntityUtils.toString( httpClient.execute( httpReq ).getEntity());

        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    private HttpRequestBase getHttpUriRequest( String host, int port, String httpMethod, String target ) {
        String uri = new StringBuilder( host )
                .append( ":" )
                .append( port )
                .append( "/" )
                .append( target ).toString();

        if ( httpMethod.equalsIgnoreCase( "GET" ) ) {
            return new HttpGet( uri );
        } else if ( httpMethod.equalsIgnoreCase( "POST" ) ) {
            return new HttpPost( uri );
        } else if ( httpMethod.equalsIgnoreCase( "DELETE" ) ) {
            return new HttpDelete( uri );
        } else if ( httpMethod.equalsIgnoreCase( "PUT" ) ) {
            return new HttpPut( uri );
        } else {
            throw new IllegalArgumentException( "param is invalid." );
        }
    }

    private void setDefaultHeaders( HttpRequestBase httpReq ) {
        if ( !(httpReq instanceof HttpGet) ) {
            httpReq.setHeader("Content-type", "application/json");
        }

        httpReq.setHeader("Accept", "application/json" );
        httpReq.addHeader( "Cache-Control", "no-cache" );
    }

    public static void main( String[] args ) {
        ConcreteInjector injector = new ConcreteInjector();

        IInvoker invoker = new RestInvoker(
                injector.getHost(),
                injector.getPort(),
                injector.getHttpClient()
        );

        String ret = invoker.invokeHttp( "df", null, "GET", null );

        System.out.println( ret );
    }
}
