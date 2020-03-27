package http.pure.base;

import http.pure.invoker.KmsInvoker;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Host( host="http://localhost", port="8080" )
@AuthInfo
public class TestBase {

	private String token;

	public String getToken() throws NoSuchAlgorithmException, JSONException, IOException {
		if ( token != null ) return token;

		String authTokenUri = "authentication/token";
		JSONObject body = new JSONObject();
		long currentTime = new Date().getTime();

		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		String hashedPw = DatatypeConverter.printHexBinary( sha256.digest( getPw().getBytes() ) ).toLowerCase();

		sha256.update( ( hashedPw + currentTime + getId() ).getBytes() );

		String authVal = DatatypeConverter.printHexBinary( sha256.digest() );

		body.put( "id", getId() );
		body.put( "pw", authVal );
		body.put( "Operation", "Create");

		KmsInvoker invoker = new KmsInvoker.Builder().host( getHost() )
				.uri( authTokenUri )
				.httpMethod( "POST" )
				.body( body.toString() )
				.xHttpRequestTime( currentTime )
				.build();

		JSONObject result = new JSONObject( invoker.invokeCreTokenKms() );
		System.out.println( result );
		this.token = result.getString( "token" );

		return token;
	}

	public String getHost() {
		Host hostInfo = this.getClass().getAnnotation( Host.class );

		return new StringBuilder( hostInfo.host() )
				.append( ":" )
				.append( hostInfo.port() ).toString();

	}

	public String getId() {
		AuthInfo authInfo = TestBase.class.getAnnotation( AuthInfo.class );

		return authInfo.id();
	}

	public String getPw() {
		AuthInfo authInfo = TestBase.class.getAnnotation( AuthInfo.class );
		
		return authInfo.pw();
	}
	
}
 