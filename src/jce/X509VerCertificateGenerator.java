package jce;

import java.security.cert.X509Certificate;

public abstract class X509VerCertificateGenerator {

	protected final String dn;
	
	protected X509VerCertificateGenerator( String dn ) {
		this.dn = dn;
	}
	
	public X509Certificate loadCertFile( byte[] certFile ) {
		
		
		return null;
	}
}
