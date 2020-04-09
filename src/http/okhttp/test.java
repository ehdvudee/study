package http.okhttp;

import http.IInvoker;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// https://developer.squareup.com/blog/okhttp-3-13-requires-android-5/
// https://github.com/square/okhttp/issues/4481
// https://stackoverflow.com/questions/48532860/is-it-thread-safe-to-make-calls-to-okhttpclient-in-parallel
public class test {

    byte[] cert = DatatypeConverter.parseBase64Binary( "MIIDgzCCAmugAwIBAgIBAjANBgkqhkiG9w0BAQsFADA/MT0wOwYDVQQDEzRHTEFTTyBSb290Q0EgT1UgPSBEZXZlbG9wZXJUZWFtIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDIzMVoXDTIyMTIxNjA2MDIzMVowQjFAMD4GA1UEAww37Jew6rWs6rCc67CcMuu2gDLtjIAgT1UgPSBtYWdpY2ttcyBPID0gR2xhc29Db3JwIEMgPSBLUjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIzFoLbXcEs743/moZuwPitZLu2ERGzq0xIEgapEJ6KTn91YDlivbs6qYvcbYiFr4xwM5zbt72APmGs1TUhdeOYpMb8Nr2Y7Ibeksp4N7yd7n7mK6vi/31MIN7J4HNHkaJUTnubIei1SRQ9HvdHZrpBUeu7kKa/wEdqQOQx++J0Z2DcnAndqsnvvfPS7AFFyEj8LcULtmHSiLyInqPZbNx15aUJU5aY+Wzr0dR9FWFlWw3z6P9IgH6qn6g6COyFSO2C0Q6YU0VfDeP8t5j0v2YIy8plMB+I0G2CbiZ2xdHgQApLiVKzziqBEpjw0z4AlcjCzdpY6ZqDlg/5yqQ+sAKECAwEAAaOBhjCBgzAfBgNVHSMEGDAWgBSJHYPWl1bYyRo1F80iCoplandW0jASBgNVHRMBAf8ECDAGAQH/AgEAMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjAOBgNVHQ8BAf8EBAMCAoQwHQYDVR0OBBYEFFEjESvibme8NY9wA4wxa51Vv+bQMA0GCSqGSIb3DQEBCwUAA4IBAQBIavT7chbxmVkJ3T48o+v+TVc8YX/qSMYSEYxfv8CnO8S/AjVMizy4tB0kcj+Y/kqknv95+63p5GpQ+YmAtdwnFT+n9tqMT3ZWU4eG7Qr9dS2HB2M/3MxVFZ73IrBiSiB21PHHjWkyKZ971PruE2It0vqMFjs3pGxCXDOvV6P0hFUootbGQZuPZ0uklHnLP9Fs0ddMGFqtycBM0PPUoA4KQylTs3+Cyh6QVDZLOFmrbdJy0Oc1T2lIKwBiBD4GG3MmXmAKXnpTxY88BOPTyAOJtuMyXT/PJXnzs0RA62azUVPq2cCx99znJNCewai+7mHvfbSWi9vWcevCKicfPzdy" );

    @Test
    public void runWithSimpleGet() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.google.com/")
                .build();

        Response resp = client.newCall(request).execute();

        System.out.println(resp);
    }

    @Test
    public void runWithHeaders() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.google.com/")
                .header("feef", "efef")
                .header("testyoyo", "byte")
                .build();


        Response resp = client.newCall(request).execute();

    }

    @Test
    public void runWithCallback() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://www.google.com").build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("kkkkkkkk loooooser.");
                } else {
                    // do something with the result.
                }
            }
        });
    }

    @Test
    public void runWithBody() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON =  MediaType.parse( "application/json; charset=utf-8" );

        RequestBody body = RequestBody.create( JSON, "{\"test\":\"test\"}" );
//        RequestBody body = RequestBody.create( null, "" );

        Request request = new Request.Builder()
                .url( "https://www.google.com" )
                .method( "POST", body )
                .build();

        Response response = client.newCall( request ).execute();
        System.out.println( response );
    }

    @Test
    public void runWithCustomHttps() throws IOException {
        MediaType JSON = MediaType.parse( "application/json; charset=utf-8" );

        Headers header = new Headers.Builder()
                .add( "Content-Type", "application/json" )
                .add( "Accept", "application/json" )
                .add( "Cache-Control", "no-cache" )
                .add( "X-HTTP-Request-Time", "1586328465952" ).build();

        JSONObject jsonBody = new JSONObject();

        jsonBody.put( "id", "kms-dream" );
        jsonBody.put( "pw",  "dbaf41b71fd32e32b4404ad2544021bbe271553b79a7c8da9ca3f63a5170f55b" );
        jsonBody.put( "authState", 0 );
        jsonBody.put( "Operation", "Create" );

        RequestBody body = RequestBody.create( JSON, jsonBody.toString() );

        Request request = new Request.Builder()
                .url( "https://10.10.30.115:8444/authentication/token" )
                .headers( header )
                .method( "POST", body )
                .build();

        SSLContext sslCtx;
        try {
            sslCtx = SSLContextUtils.getSSLContext(cert, "SSL", false);
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        HostnameVerifier hostNameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory( sslCtx.getSocketFactory() )
                .hostnameVerifier( hostNameVerifier )
                .build();

        Response resp = client.newCall( request ).execute();
        System.out.println( resp );
        System.out.println( resp.body().string() );
    }

    @Test
    public void runWithInvoker() {
        IInvoker invoker = new RestInvoker();
        String host = "https://10.10.30.115:8444";
        String path = "authentication/token";
        String httpMethod = "POST";


        //init
        OkHttpFactory.getInstance().getHttpClient( host, cert );

        //header
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put( "X-HTTP-Request-Time", "1586328465952" );
//        headerMap.put( "Authorization", "d0700fa9cbf840e6bb324bf4c4ed3894fea4930f" );

        //body
        JSONObject jsonBody = new JSONObject();

        jsonBody.put( "id", "kms-dream" );
        jsonBody.put( "pw",  "dbaf41b71fd32e32b4404ad2544021bbe271553b79a7c8da9ca3f63a5170f55b" );
        jsonBody.put( "authState", 0 );
        jsonBody.put( "Operation", "Create" );

        String respStr = invoker.invokeHttp( host, path, httpMethod, headerMap, jsonBody.toString() );
        System.out.println( respStr );
    }
}
