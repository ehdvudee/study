package http.invoker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class KmsInvoker {
	private final String host;
	private final String uri;
	private final String httpMethod;
	private final HttpInvoker httpInvoker;
	
	private String body;
	private long xHttpRequestTime = 0;
	private String authToken = null;
	
	private KmsInvoker( Builder builder ) {
		this.host = builder.host;
		this.uri = builder.uri;
		this.httpMethod = builder.httpMethod;
		this.body = builder.body;
		this.httpInvoker = new HttpInvoker();
		
		if ( builder.xHttpRequestTime != 0 ) xHttpRequestTime = builder.xHttpRequestTime;
		if ( builder.authToken != null ) authToken = builder.authToken;
	}

	public String invokeCommonKms() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) initUrl( host, uri ).openConnection();

		return httpInvoker.invokeKMS( conn, httpMethod, body );
	}
	
	public String invokeAuthorizedKms() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) initUrl( host, uri ).openConnection();
		conn.setRequestProperty("Authorization", authToken );
		
		return httpInvoker.invokeKMS( conn, httpMethod, body );
	}
	
	public String invokeCreTokenKms() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) initUrl( host, uri ).openConnection();
		conn.setRequestProperty("X-HTTP-Request-Time", String.valueOf( xHttpRequestTime ) );
		
		return httpInvoker.invokeKMS( conn, httpMethod, body );
	}
	
	private URL initUrl( String host, String uri ) throws IOException {
		return new URL( new StringBuilder( host )
				.append( "/" )
				.append( uri ).toString() );
	}
	
	public static class Builder {

		private String host;
		private String uri;
		private String httpMethod;
		
		private String body;
		private long xHttpRequestTime;
		private String authToken;

		public Builder host( String host ) { this.host = host; return this; }
		public Builder uri( String uri ) { this.uri = uri; return this; }
		public Builder httpMethod( String httpMethod ) { this.httpMethod = httpMethod; return this; } 
		public Builder body( String body ) { this.body = body; return this; } 
		public Builder xHttpRequestTime( long time ) { this.xHttpRequestTime = time; return this; }
		public Builder authToken( String token ) { this.authToken = token; return this; } 

		public KmsInvoker build() {
			if ( host == null || uri == null || httpMethod == null ) 
				throw new NullPointerException( "host, uri or httpMethod is null." );
			
			return new KmsInvoker( this );
		}
	}
}
