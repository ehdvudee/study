package jce;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;

import javax.xml.bind.DatatypeConverter;

import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs10.PKCS10;
import sun.security.pkcs10.PKCS10Attribute;
import sun.security.pkcs10.PKCS10Attributes;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.DNSName;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.X500Name;


public class Sample008CreateCSR {
	public static void main( String[] args ) throws NoSuchAlgorithmException, IOException, InvalidKeyException, CertificateException, SignatureException {

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );		
		keyGen.initialize( 2048, SecureRandom.getInstance("SHA1PRNG") );

		KeyPair keyPair = keyGen.generateKeyPair();

		X500Name x500Name = new X500Name( "CN=Example.com" );

		Signature sig = Signature.getInstance( "SHA256WithRSA" );

		sig.initSign( keyPair.getPrivate() );

		// PKCS 9 Attribute init ( 초기화 할 때만, 적용할 수 있음 ㅡㅡ )
		// https://stackoverflow.com/questions/49985805/add-key-usage-to-certificatesigninginfo-in-java
		CertificateExtensions ext = new CertificateExtensions();
		GeneralNames subjectAltName = new GeneralNames();
		subjectAltName.add( new GeneralName( new DNSName( "naver.com" ) ) );
		subjectAltName.add( new GeneralName( new DNSName( "naver.com" ) ) );
		ext.set( SubjectAlternativeNameExtension.NAME, new SubjectAlternativeNameExtension( false, subjectAltName ) );
		PKCS10Attribute pkcs10Attr = new PKCS10Attribute (PKCS9Attribute.EXTENSION_REQUEST_OID, ext);

		PKCS10 pkcs10 = new PKCS10( keyPair.getPublic(), new PKCS10Attributes( new PKCS10Attribute[]{ pkcs10Attr } ) );
		// for java 6
//		pkcs10.encodeAndSign(new X500Signer(sig, x500Name ) );
		// more than java 7 
		pkcs10.encodeAndSign(x500Name, sig);

		System.out.println( pkcs10 );

		System.out.println( new PKCS10( pkcs10.getEncoded() ) ); 

		//TestVerctor
		PKCS10 pkcs10_2 = new PKCS10( DatatypeConverter.parseBase64Binary( "MIIDYDCCAkgCAQAwgdAxCzAJBgNVBAYTAlVTMREwDwYDVQQIDAhOZXcgWW9yazESMBAGA1UEBwwJUm9jaGVzdGVyMRIwEAYDVQQKDAlFbmQgUG9pbnQxFzAVBgNVBAsMDlRlc3RpbmcgRG9tYWluMUswSQYJKoZIhvcNAQkBFjx5b3VyLWFkbWluaXN0cmF0aXZlLWFkZHJlc3NAeW91ci1hd2Vzb21lLWV4aXN0aW5nLWRvbWFpbi5jb20xIDAeBgNVBAMMF3d3dy55b3VyLW5ldy1kb21haW4uY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt4W+4t4GhXIlkb46NAJMihUx8LNhICDT1mSvZmPyVM3GxUIw4yf1wOpXadXDfR1dXURa51GipVWvvrZhslGVEmPVpeprORMc4whYqEyrU7IBTt8sXDZZJIdn0A5X4j5Apft/N2DFuf34KflO0c7XvhHDmWmzjdCkXZ9C0ClHa8bejoVMKD7DhLgsGbm8By29nZnPXA9pFo26kVYfDhDQ6Pi+qjh6h68sCgOjBWrfVhgslIAftQlfuirO6fSNlCsr9DueJI2ejlsDi/CwVYaicYvY3MI6GOACHXQ89C2Bv6TTGIr/O9zMZNht7fHEm5P6f1jz1PzlCswusworEQH36wIDAQABoEowSAYJKoZIhvcNAQkOMTswOTA3BgNVHREEMDAughN5b3VyLW5ldy1kb21haW4uY29tghd3d3cueW91ci1uZXctZG9tYWluLmNvbTANBgkqhkiG9w0BAQsFAAOCAQEAO/XnlqDixU+nADkW9z935ND7UyxS+ggmVNE+A0WLPQWIFaI6EdqmwBqohZAlcliXFFQWXP7OzBbzLziwmJ3vEkxcuxxMdct0LTb0ps5Y7eo9Z1aw8aIKw/8r15yMmwW3WxKaaZVBZFZ32w1D5SqbgykUsvRfExUNLTNEJaFVKiXFsNaCkWXpuUkX1IeigMKZo/qCPok1Jh5Lfs2Phyz1+1fjy/TSERcwQUvVB/VTlxbbLmIQg7YounSCmeeBnODCdvA413hDUb+T62UPX+PZ6KhD4WQOMU9Z7KwUjpSd9xOmJLbCzz/v6chANnZSZn1FldTZpLds+mJ7+YoPs96AOQ==" ) );
		System.out.println( pkcs10_2 );

	}

}

