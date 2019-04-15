package pbkdf.api;

import java.security.Provider;
import java.security.Security;

import pbkdf.lib.PBKDF2Engine;

public class Pbkdf2Util {
	
	private final byte[] password;
	private final byte[] salt;
	private final int iteration;
	private final String hashAlgoritm;
	private final int derivedKeyLength;
	private final Provider provider;
	
	public static class Builder {
		private byte[] password;
		private byte[] salt;
		private int iteration;
		private String hashAlgoritm = "HmacSHA256";
		private int derivedKeyLength = 64;
		private Provider provider = Security.getProvider( "SunJCE" );
		
		public Builder(){}
		
		public Builder( byte[] password, byte[] salt, int iteration ) {
			this.password = password;
			this.salt = salt;
			this.iteration = iteration;
		}
		
		public Builder password( String password ) {
			this.password = this.convertHexStringToByteArray( password ); return this;
		}
		public Builder password( byte[] password ) {
			this.password = password; return this;
		}
		public Builder salt( byte[] salt ) {
			this.salt = salt; return this;
		}
		public Builder salt( String salt ) {
			this.salt = this.convertHexStringToByteArray( salt ); return this;
		}
		public Builder iteration( int iteration ) {
			this.iteration = iteration; return this;
		}
		public Builder deriveAlgoritm( String hashAlgoritm ) {
			this.hashAlgoritm = hashAlgoritm; return this;
		}
		public Builder derivedKeyLength( int derivedKeyLength ) {
			this.derivedKeyLength = derivedKeyLength; return this;
		}
		public Builder provider( String provider ) {
			this.provider = Security.getProvider( provider ); return this;
		}
		public Builder provider( Provider provider ) {
			this.provider = provider; return this;
		}
		
		private byte[] convertHexStringToByteArray(String hexString) {
			int len = hexString.length();
			byte[] data = new byte[len / 2];

			for (int i = 0; i < len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
						+ Character.digit(hexString.charAt(i + 1), 16));
			}

			return data;
		}
		
		public Pbkdf2Util build() {
			return new Pbkdf2Util( this );
		}
	}
	
	private Pbkdf2Util( Builder builder ) {
		password = builder.password;
		salt = builder.salt;
		iteration = builder.iteration;
		hashAlgoritm = builder.hashAlgoritm;
		derivedKeyLength = builder.derivedKeyLength;
		provider = builder.provider;
	}
	
	public byte[] derivKey() {
		return new PBKDF2Engine( hashAlgoritm, provider.getName() ).deriveKey( password, salt, iteration, derivedKeyLength );
	}
}
