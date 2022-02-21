package jce;

import hkdf.HKDF;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Sample025ConvergentEnc {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyval = DatatypeConverter.parseHexBinary("2b8f23a3477cb23a44534cee5cc5148f30d534d5ddd5316751dfada6b2e8fe49");
        byte[] context = DatatypeConverter.parseHexBinary("a1");
        byte[] plaintext = DatatypeConverter.parseHexBinary("48656c6c6f20576f726c640a");

        HKDF hkdf = HKDF.fromHmacSha256();
        byte[] pseudoRandomKey = HKDF.fromHmacSha256().extract((byte[]) null, keyval);
        byte[] outputKeyingMaterial = HKDF.fromHmacSha256().expand(pseudoRandomKey, context, 64);

        byte[] hmacKey = new byte[32];
        System.arraycopy(outputKeyingMaterial, 32, hmacKey, 0, 32);

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(hmacKey, "RawBytes"));
        byte[] ivMaterial = mac.doFinal(plaintext);
        byte[] iv = new byte[12];

        System.arraycopy(ivMaterial, 0, iv, 0, 12);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        byte[] key = new byte[32];
        System.arraycopy(outputKeyingMaterial, 0, key, 0, 32);
        SecretKey objKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, objKey, gcmParameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext);

        System.out.println(DatatypeConverter.printHexBinary(outputKeyingMaterial));
        System.out.println(DatatypeConverter.printHexBinary(iv));
        System.out.println(DatatypeConverter.printHexBinary(ciphertext));
    }
}
