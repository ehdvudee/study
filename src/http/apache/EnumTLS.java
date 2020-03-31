package http.apache;

public enum EnumTLS {
	SSL( "SSL" ),
	// common java se 6 is not avaiable TLSv1.1 and TLSv1.2
	// supported java se 6 is avaiable( TLSv1.1 : from u111, TLSv1.2 : from u121 )
	// supproted common java 7 or higher
	TLS_V_1_1( "TLSv1.1" ),
	TLS_V_1_2( "TLSv1.2" );
	
	private String protocol;
	
	EnumTLS( String protocol ) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
}
