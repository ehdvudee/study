package jce;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class Sample002EncDec4_2 {
	public static void main( String[] args ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {

		byte[] plaintext = DatatypeConverter.parseHexBinary("a1a1a1a1a1a1a1a1a1a1a1a1");
		byte[] iv = DatatypeConverter.parseHexBinary("a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1");
		byte[] key = DatatypeConverter.parseHexBinary("a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1");
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);

		// Generate Key
		SecretKey objKey = new SecretKeySpec(key, "AES");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, objKey, gcmParameterSpec);
		byte[] ciphertext = cipher.doFinal(plaintext);

		System.out.println(DatatypeConverter.printHexBinary(ciphertext));

		cipher.init(Cipher.DECRYPT_MODE, objKey, gcmParameterSpec);
		byte[] plain = cipher.doFinal(ciphertext);
		System.out.println(DatatypeConverter.printHexBinary(plain));

	}
}
