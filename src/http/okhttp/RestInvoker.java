package http.okhttp;

import http.IInvoker;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class RestInvoker implements IInvoker {

    MediaType JSON = MediaType.parse( "application/json; charset=utf-8" );

    @Override
    public String invokeHttp( String host, String path, String httpMethod, Map<String, String> headers, String body ) {
        OkHttpClient client = OkHttpFactory.getInstance().getHttpClient( host );

        Headers.Builder headerBuilder = new Headers.Builder()
                .add( "Content-Type", "application/json" )
                .add( "Accept", "application/json" )
                .add( "Cache-Control", "no-cache" );

        if ( headers != null ) {
            for ( String key : headers.keySet() ) {
                headerBuilder.add( key, headers.get( key ) );
            }
        }

        Headers headerSet = headerBuilder.build();

        RequestBody bodySet;
        if ( body != null && !httpMethod.equalsIgnoreCase( "GET" ) ) bodySet = RequestBody.create( JSON, body );
        else bodySet = null;

        host = new StringBuilder( host )
                .append( "/" )
                .append( path ).toString();

        Request request = new Request.Builder()
                .url( host )
                .headers( headerSet )
                .method( httpMethod, bodySet )
                .build();

        try {
            Response resp = client.newCall(request).execute();

            return resp.body().string();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
}
