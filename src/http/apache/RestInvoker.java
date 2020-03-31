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

    public RestInvoker( String host, int port ) {
        this( host, port, null );
    }

    public RestInvoker( String host, int port, byte[] cert ) {
        this( host, port, cert, true );
    }

    public RestInvoker( String host, int port, byte[] cert, boolean hostChk ) {
        this.host = host;
        this.port = port;

        try {
            if (cert != null && host.contains( "https://" ) ) {
                this.httpClient = HttpClientFactory.getInstance(cert, hostChk).getHttpClient(host, port);
            } else {
                this.httpClient = HttpClientFactory.getInstance().getHttpClient(host, port);
            }
        } catch (Exception e) {
            throw new RuntimeException("init error", e);
        }
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
        String host = "https://10.10.30.115";
        int port = 8443;
        byte[] cert = DatatypeConverter.parseBase64Binary( "MIIDczCCAlugAwIBAgIBCTANBgkqhkiG9w0BAQsFADBCMUAwPgYDVQQDDDfsl7DqtazqsJzrsJwy67aAMu2MgCBPVSA9IG1hZ2lja21zIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDU0NFoXDTIwMTIyMjA2MDU0NFowOjE4MDYGA1UEAxMvTyA9IERyZWFtLUtNUy1Db3JlIEwgPSBTZW91bCBTID0gU29uZy1QYSBDID0gS1IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCwnjlbJnB26XoqcF/XIwAiOz0Y55TDoJQ7aPLv7vFRWFJUAWFHQRJwSNA2RdJDyrYFihAEWkmRP6uKnrBcs9BTr9uCTabYJJ39R4DkqzMA48HqkehRmNlmJSfMeba7hXkp6MUFJfXF+/PJZAZZuoXXSGaQZG0G63xaYsYnzCdjB+8aCc1lMQOiuUY9hDBL210v+//O9jnnIPM3R8VHAkQ2+aViRJwJuJbqtZTkSo6tQ06WwCfm+C+nBVnmpXwyn54ZNuY+ZsDyXJNIhmVu4/qoXqfAKldMABG+mBeTGl2je8vCG30yL32YunRAdesydo+CtTfaGFfNbYEPAgox9Ao3AgMBAAGjfDB6MB8GA1UdIwQYMBaAFFEjESvibme8NY9wA4wxa51Vv+bQMAkGA1UdEwQCMAAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4EFgQUeDM5qKyPOeb0t/hDtZWEvGY3QfMwDQYJKoZIhvcNAQELBQADggEBABBoczg8Nq8/G8WA+ts4q/E6/+HJdcOr783QcmqnMST0LUzbzbdp0dbKI32TQfISkk31CSj7zbdz9ddHJC5LsO4QFJbYPtfQr0/4YxQOTsu6LBBXVShexv6gX7OBSEaIOLy+jPh5VGi3hxjX4TZINJZvHasJoFGn7fvufYbCjVgJwBcxnRIJ4a/cEOdLOCRVUW/UXSMTq6M5LS5yABGdlyH6yrJYxV7ZKEExU7nw7Sp78O+ku6Ao8KwTyIN3VhXT2qIV6egnIkwwAFltHq/kZliTz3odiP6CqmeYfpLv5JGReZ/M7wA+224/jTraCLYRmRMdn8cdfwnCnCDmeJQfPvU=" );

        IInvoker invoker = new RestInvoker( host, port, cert );
        String ret = invoker.invokeHttp( "df", null, "GET", null );

        System.out.println( ret );
    }
}
