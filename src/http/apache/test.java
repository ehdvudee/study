package http.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class test {
	
	private String host = "http://localhost:8081";
	
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
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
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
				
			}
		};
		
		String httpResponse = httpClient.execute( httpGet, responseHandler );
		System.out.println( httpResponse );
	}
	
	private String streamToString( InputStream inStream ) throws IOException {
		StringBuilder textBuilder = new StringBuilder();
		Reader reader = new BufferedReader( new InputStreamReader( inStream, "UTF-8" ) );
		try {
			int c = 0;
			while ( (c = reader.read() ) != -1) {
				textBuilder.append((char) c);
			}
		} finally {
			reader.close();
		}
		
		return textBuilder.toString();
	}
}
