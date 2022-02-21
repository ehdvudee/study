package hkdf;

import javax.xml.bind.DatatypeConverter;

public class HkdfTest {
    public static void main(String[] args) {
        byte[] keyval = DatatypeConverter.parseHexBinary("2b8f23a3477cb23a44534cee5cc5148f30d534d5ddd5316751dfada6b2e8fe49");
        byte[] context = DatatypeConverter.parseHexBinary("a1");
        byte[] plaintext = DatatypeConverter.parseHexBinary("48656c6c6f20576f726c640a");

        HKDF hkdf = HKDF.fromHmacSha256();
        byte[] pseudoRandomKey = HKDF.fromHmacSha256().extract((byte[]) null, keyval);
        byte[] outputKeyingMaterial = HKDF.fromHmacSha256().expand(pseudoRandomKey, context, 64);

        System.out.println("ret: " + DatatypeConverter.printHexBinary(outputKeyingMaterial));
    }
}
