package http.apache;


import org.apache.http.client.HttpClient;

import javax.xml.bind.DatatypeConverter;

public class ConcreteInjector {
    private final String hostAl = "https://10.10.30.115";
    private final String hostBe = "https://10.10.30.115";
    private final String hostCh = "http://10.10.30.115";
    private final String hostDe = "http://10.10.30.115";

    private final int portAl = 8443;
    private final int portBe = 8444;
    private final int portCh = 8380;
    private final int portDe = 8280;

    private final byte[] certAl = DatatypeConverter.parseBase64Binary( "MIIDczCCAlugAwIBAgIBCTANBgkqhkiG9w0BAQsFADBCMUAwPgYDVQQDDDfsl7DqtazqsJzrsJwy67aAMu2MgCBPVSA9IG1hZ2lja21zIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDU0NFoXDTIwMTIyMjA2MDU0NFowOjE4MDYGA1UEAxMvTyA9IERyZWFtLUtNUy1Db3JlIEwgPSBTZW91bCBTID0gU29uZy1QYSBDID0gS1IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCwnjlbJnB26XoqcF/XIwAiOz0Y55TDoJQ7aPLv7vFRWFJUAWFHQRJwSNA2RdJDyrYFihAEWkmRP6uKnrBcs9BTr9uCTabYJJ39R4DkqzMA48HqkehRmNlmJSfMeba7hXkp6MUFJfXF+/PJZAZZuoXXSGaQZG0G63xaYsYnzCdjB+8aCc1lMQOiuUY9hDBL210v+//O9jnnIPM3R8VHAkQ2+aViRJwJuJbqtZTkSo6tQ06WwCfm+C+nBVnmpXwyn54ZNuY+ZsDyXJNIhmVu4/qoXqfAKldMABG+mBeTGl2je8vCG30yL32YunRAdesydo+CtTfaGFfNbYEPAgox9Ao3AgMBAAGjfDB6MB8GA1UdIwQYMBaAFFEjESvibme8NY9wA4wxa51Vv+bQMAkGA1UdEwQCMAAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4EFgQUeDM5qKyPOeb0t/hDtZWEvGY3QfMwDQYJKoZIhvcNAQELBQADggEBABBoczg8Nq8/G8WA+ts4q/E6/+HJdcOr783QcmqnMST0LUzbzbdp0dbKI32TQfISkk31CSj7zbdz9ddHJC5LsO4QFJbYPtfQr0/4YxQOTsu6LBBXVShexv6gX7OBSEaIOLy+jPh5VGi3hxjX4TZINJZvHasJoFGn7fvufYbCjVgJwBcxnRIJ4a/cEOdLOCRVUW/UXSMTq6M5LS5yABGdlyH6yrJYxV7ZKEExU7nw7Sp78O+ku6Ao8KwTyIN3VhXT2qIV6egnIkwwAFltHq/kZliTz3odiP6CqmeYfpLv5JGReZ/M7wA+224/jTraCLYRmRMdn8cdfwnCnCDmeJQfPvU=" );
    private final byte[] certBe = DatatypeConverter.parseBase64Binary( "MIIDczCCAlugAwIBAgIBCzANBgkqhkiG9w0BAQsFADBCMUAwPgYDVQQDDDfsl7DqtazqsJzrsJwy67aAMu2MgCBPVSA9IG1hZ2lja21zIE8gPSBHbGFzb0NvcnAgQyA9IEtSMB4XDTE5MTIyMzA2MDg1MloXDTIwMTIyMjA2MDg1MlowOjE4MDYGA1UEAxMvTyA9IERyZWFtLUtNUy1BdXRoIEwgPSBTZW91bCBTID0gU29uZy1QYSBDID0gS1IwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCqYQxLZsKrpZPxoa9qsCm0P9C3lay/hGXlE+wbpn4Cs7vx98hik9GHt+u5dxe4Um3DcImCvN28YdfHfoS+/P0yIiEQZ9e2ICFrhrwPcGndg2x1cEyK1mZGuLYlPePZFmdNPe56nH6qj3A2OeTpcacSrKgFTnz7BckBuHZnWG8WfdlEape91AeZlt4OlkeM+WKezrlX6o0EmIZ2hM16/ZmkQ6gPfc1lmESo1/VHUFaSWsC/vpYPBuOiXuD6cs89qCMzX8lhcXlSLghWORIYTrXEs4JcJuNGPFQuRJinAla31PVBmvyj6PWII4oOm0bCDHaH+xlPFtgObNqZ6cU10RQZAgMBAAGjfDB6MB8GA1UdIwQYMBaAFFEjESvibme8NY9wA4wxa51Vv+bQMAkGA1UdEwQCMAAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4EFgQUqFAISmEGpbwuplp4R5PA0zo/Jf0wDQYJKoZIhvcNAQELBQADggEBABQuFP/8RdAqjjRcq68g1pXlS06hPzvTbEswQB5Zxv6obFhR29JEe0bQImON76WXtU5km8BjdsgGI9atQWWbo1V57G0IfsnWTn7bKDSdl+ljeAhcnNFkJSKb9CQsGZ+nsHb+bTPRRx4tZzmtHTOXZhc9zGFFPjMf0zdvPJgKeH0GL3wUGndb3lLi/pJXvSg1io0hA08X/MfgL2/lbaKdXeAyrw1yzgSWCNPMEZc8uWGALmp7rqf/cQnU2q7IkgXAY9QQ+kkl7HLhoIJaylNkKHUtjTxOcsx10PJzk1Wl3tJ89vHtL2/4B06Akp0vvTdOwYyb3B6uQl/CIu+c8cckvNo=" );

    public ConcreteInjector() {
        init();
    }

    public void init() {
        try {
            HttpClientFactory.getInstance().getHttpClient( hostAl, portAl, certAl );
            HttpClientFactory.getInstance().getHttpClient( hostBe, portBe, certBe );
            HttpClientFactory.getInstance().getHttpClient( hostCh, portBe );
            HttpClientFactory.getInstance().getHttpClient( hostDe, portBe );

        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public String getHostAl() { return this.hostAl; }
    public String getHostBe() { return this.hostBe; }
    public String getHostCh() { return this.hostCh; }
    public String getHostDe() { return this.hostDe; }

    public int getPortAl() { return this.portAl; }
    public int getPortBe() { return this.portBe; }
    public int getPortCh() { return this.portCh; }
    public int getPortDe() { return this.portDe; }

    public HttpClient getHttpClient( String host, int port ) {
        try {
            return HttpClientFactory.getInstance().getHttpClient(host, port);
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}
