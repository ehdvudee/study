package jce.cert;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import sun.security.util.DerValue;
import sun.security.x509.DNSName;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;

public class CertGeneratorFactory {

	public Builder getBuilder() {
		return new Builder();
	}

	public class Builder {
		private KeyPair subject;
		private GeneralNames subjectAltName;
		private PrivateKey issuer;
		private long validity;
		private String algorithm;
		private X509Certificate issuerCert;
		private String dn;
		private BigInteger serialNumber;
		private Class<?> t;
		
		public Builder subject( KeyPair subject ) { this.subject = subject; return this; }
		public Builder issuer( PrivateKey issuer ) { this.issuer = issuer; return this; }
		public Builder validity( long validity ) { this.validity = validity; return this;}
		public Builder algorithm( String algorithm ) { this.algorithm = algorithm; return this;}
		public Builder issuerCert( X509Certificate issuerCert ) { this.issuerCert = issuerCert; return this; }
		public Builder dn( String dn ) { this.dn = dn; return this; }
		public Builder serialNumber( BigInteger serialNumber ) { this.serialNumber = serialNumber; return this; }
		public Builder type( Class<?> t ) { this.t= t; return this; }
		public Builder subjectAltName( List<String> dnsNames ) throws IOException {
			this.subjectAltName = new GeneralNames();
			
			for ( String dnsName : dnsNames ) {
				if ( dnsName.indexOf( "*" ) >= 0 ) {
					DerValue wildcard = new DerValue( DerValue.tag_IA5String, dnsName );
					subjectAltName.add( new GeneralName( new DNSName( wildcard ) ) );
				} else {
					subjectAltName.add( new GeneralName( new DNSName( dnsName ) ) );
				}
			}
			
			return this; 
		}
		
		public KeyPair getSubject() { return subject; }
		public GeneralNames getSubjectAltName() { return subjectAltName; }
		public PrivateKey getIssuer() { return issuer; }
		public long getValidity() { return validity; }
		public String getAlgorithm() { return algorithm; }
		public X509Certificate getIssuerCert() { return issuerCert; }
		public String getDn() { return dn; } 
		public BigInteger getSerialNumber() { return serialNumber; }
		
		public X509V3CertGenerator build()  {
			return newInstance( this, t );
		}
	}

	private X509V3CertGenerator newInstance( Builder builder, Class<?> type ) {
		if ( UserCertGenerator.class.isAssignableFrom( type ) ) {
			return new UserCertGenerator( builder );
		} else if ( CaCertGenerator.class.isAssignableFrom( type ) ) {
			return new CaCertGenerator( builder );
		} else if ( RootCertGenerator.class.isAssignableFrom( type ) ) {
			return new RootCertGenerator( builder );
		} else {
			throw new IllegalArgumentException( "Cert type is Illegal.");
		}
	}
}
