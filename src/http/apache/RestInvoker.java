package http.apache;

import http.IInvoker;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class RestInvoker implements IInvoker {

    @Override
    public String invokeHttp( String host, String path, String httpMethod, Map<String, String> headers, String body ) {

        try {
            HttpClient httpClient = HttpClientFactory.getInstance().getHttpClient( host );
            HttpRequestBase httpReq = getHttpUriRequest( host, path, httpMethod );

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

    private HttpRequestBase getHttpUriRequest( String host, String path, String httpMethod ) {
        String uri = new StringBuilder( host )
                .append( "/" )
                .append( path ).toString();

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

        String host = injector.getHostAl();
        IInvoker invoker = injector.getInvoker();

        String ret = invoker.invokeHttp( host, "/company/user-info/ehdvukadee", "GET", null,null );

        System.out.println( ret );
    }
}
