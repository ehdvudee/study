package http.apache;


import org.apache.http.client.HttpClient;

import javax.xml.bind.DatatypeConverter;

public class ConcreteInjector {
    private final String host = "https://10.10.30.115";
    private final int port = 8443;

    private final byte[] cert = DatatypeConverter.parseBase64Binary( "MIIDczCCAlugAwIBAgIBCTANBgkqhkiG9w0BAQsFADBCMUAwPgYDVQQDDDfsl7DqtazqsJzrsJwy67aAMu2MgCBPVSA9IG1hZ2lja21zIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDU0NFoXDTIwMTIyMjA2MDU0NFowOjE4MDYGA1UEAxMvTyA9IERyZWFtLUtNUy1Db3JlIEwgPSBTZW91bCBTID0gU29uZy1QYSBDID0gS1IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCwnjlbJnB26XoqcF/XIwAiOz0Y55TDoJQ7aPLv7vFRWFJUAWFHQRJwSNA2RdJDyrYFihAEWkmRP6uKnrBcs9BTr9uCTabYJJ39R4DkqzMA48HqkehRmNlmJSfMeba7hXkp6MUFJfXF+/PJZAZZuoXXSGaQZG0G63xaYsYnzCdjB+8aCc1lMQOiuUY9hDBL210v+//O9jnnIPM3R8VHAkQ2+aViRJwJuJbqtZTkSo6tQ06WwCfm+C+nBVnmpXwyn54ZNuY+ZsDyXJNIhmVu4/qoXqfAKldMABG+mBeTGl2je8vCG30yL32YunRAdesydo+CtTfaGFfNbYEPAgox9Ao3AgMBAAGjfDB6MB8GA1UdIwQYMBaAFFEjESvibme8NY9wA4wxa51Vv+bQMAkGA1UdEwQCMAAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4EFgQUeDM5qKyPOeb0t/hDtZWEvGY3QfMwDQYJKoZIhvcNAQELBQADggEBABBoczg8Nq8/G8WA+ts4q/E6/+HJdcOr783QcmqnMST0LUzbzbdp0dbKI32TQfISkk31CSj7zbdz9ddHJC5LsO4QFJbYPtfQr0/4YxQOTsu6LBBXVShexv6gX7OBSEaIOLy+jPh5VGi3hxjX4TZINJZvHasJoFGn7fvufYbCjVgJwBcxnRIJ4a/cEOdLOCRVUW/UXSMTq6M5LS5yABGdlyH6yrJYxV7ZKEExU7nw7Sp78O+ku6Ao8KwTyIN3VhXT2qIV6egnIkwwAFltHq/kZliTz3odiP6CqmeYfpLv5JGReZ/M7wA+224/jTraCLYRmRMdn8cdfwnCnCDmeJQfPvU=" );
    private final boolean hostChk = true;

    private HttpClient httpClient;

    public String getHost() { return this.host; }

    public int getPort() { return this.port; }

    public byte[] getCert() { return this.cert; }

    public HttpClient getHttpClient() {
        if ( httpClient == null ) {
            synchronized( this ) {
                if ( httpClient == null ) {
                    try {
                        if (cert != null) {
                            this.httpClient = HttpClientFactory.getInstance(cert, hostChk).getHttpClient(host, port);
                        } else {
                            this.httpClient = HttpClientFactory.getInstance().getHttpClient(host, port);
                        }

                        return this.httpClient;
                    } catch ( Exception e ) {
                        throw new RuntimeException( e );
                    }
                }
            }
        }

        return this.httpClient;
    }

}
