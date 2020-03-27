package http.pure.sample;

import http.pure.base.TestBase;
import http.pure.invoker.KmsInvoker;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GetKey {
	
	TestBase tb;
	
	@Before
	public void init() throws NoSuchAlgorithmException, JSONException, IOException {
		tb = new TestBase();
		tb.getToken();
	}
	
	@Test
	public void sample001() throws IOException, NoSuchAlgorithmException, JSONException {

		// GIVEN
		String uuid = "f21ecc14-1c79-4458-b4f5-fffc6afaf541";

		/* WHEN */
		KmsInvoker invoker = new KmsInvoker.Builder().host( tb.getHost() )
				.uri( "/keys/symmetrickey/" + uuid + "?Operation=Get" )
				.httpMethod( "GET" )
				.authToken( tb.getToken() )
				.build();

		JSONObject ret = new JSONObject( invoker.invokeAuthorizedKms() );

		// THEN
		System.out.println( ret );
		assertThat( 
				ret.getString( "status" ),
				equalTo( "success" )
		);
	}
}
