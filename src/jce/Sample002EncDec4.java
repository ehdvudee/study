package jce;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class Sample002EncDec4 {
	public static void main( String[] args ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {

	    byte[] rHex = DatatypeConverter.parseHexBinary( "74181299052659d2e1c6dcb7753e1921213dbf25451ba18815f039032818a494aadf4aa2d7fc493189fa9845b2745031b95a5a45712d8afc85beddfbf983a59fce9ae6be6a6ef7013e99f62f163de5a9ed5662775d9929d8f357bd10fecec684c56604be67a6b5ed667f91aded190a3b00008b4d49f701f18990870fd6993c36a59c9c52f8589fe5de689ac46dc4477985e2269018c6d51660caf6e72f5a05db11f83e520b876cc08b7255113a179551fa90ace6352dd660f93613b1a4ef15a451d8430da42727eee0a3564df2f5ffdf5cef8221f835aefe28b99afda27fee7e3c442f370a21c7aae537bab9c0ffc6815631e6e21d06d6fd8f2cadf53d313e0167877b7fe6147e18dc0e25ce41b81fedeb64c55adb92f47a732e1e5008f6cb1491d8648b255a8483ae5acce7f118a566cf8711e558f6703b3d6752e8c837522de466dbe39cf8c95022381d4b934a846c94ee18490cbd224a3230220015ffcd3945533d73d4b64c05599b2090a965826af591fa76b7940b1ae1ccd58ed44419d8f5c8c1e4603617fbd3a99b3ad2b3277eb4b2c596d903866b5cf6d5ebc53ff1aee09f48535803cd8014dd0a4fce4ff52ebaa76e81badbef3412e778947b5c98cea73453de16aa8ecdf13102d0188c535b8a8a67f51ec61da1a6a0aee698c21fd703719e38d3d10554a0dd88fb24c480f863df87b6bf504ba8eb138c4fbdf372c8e63ecad01fb9a058720696696106c829663bd4d86d061d9ed27d840fbd5c6e28e32de33858f658b334cbb43c1abca9f25e52eb5ea6f9f8187dbb000b473638872823bf9def8000196771807bfeeffaf4cf8d69d938e2d803b956d7ccb5f9883da18f9977765df7707a3be3b2f97917d289df9894bdee548fcb0203a93da9890d" );

		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance( "RSA" );
		
		SecureRandom secureRandom = new SecureRandom();
		int keyBitSize = 4096;
		keyGenerator.initialize( keyBitSize, secureRandom );
		
		KeyPair keypair = keyGenerator.generateKeyPair();
		
		Cipher cipher = Cipher.getInstance( "RSA" );
		 
		cipher.init( Cipher.ENCRYPT_MODE, keypair.getPublic() );

		byte[] ciphertext = cipher.doFinal( rHex );
		
		cipher.init( Cipher.DECRYPT_MODE, keypair.getPrivate() );
		System.out.println( new String( cipher.doFinal( ciphertext ) ) );


	}
}
