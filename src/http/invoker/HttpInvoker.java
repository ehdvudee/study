package http.invoker;



import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class HttpInvoker {
	
	public String invokeKMS( HttpURLConnection conn, String httpMethod, String body ) throws IOException {

		OutputStream os = null;
		InputStream in = null;
		String result = null;

		try {
			conn.setRequestMethod( httpMethod );
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			if ( httpMethod.equals( "DELETE" ) ) {
				// We have to override the post method so that we send data, because under jdk 1.8 version always occur JDK-7157360 error
				conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
				conn.setRequestMethod("POST");
			}

			if ( body != null ) {
				conn.setDoInput(true);
				os = conn.getOutputStream();
				os.write( body.getBytes() );
			} 

			// read the response
			if ( conn.getResponseCode() == 200 /*OK*/ || conn.getResponseCode() ==201 /*Created*/ )
				in = new BufferedInputStream( conn.getInputStream() );
			else {
				in = new BufferedInputStream( conn.getErrorStream() );
			}

			result = IOUtils.toString(in, "UTF-8");

		} finally {
			if ( os != null ) try { os.close(); } catch ( IOException e ) {}
			if ( in != null) try { in.close(); } catch ( IOException e ) {}
			if ( conn != null ) try { conn.disconnect(); } catch ( Exception e ) {}	
		}

		return result;
	}
}
