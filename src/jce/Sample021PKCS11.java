package jce;

import sun.security.pkcs11.SunPKCS11;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Sample021PKCS11 {
    public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {

        // way - 2
        File tmpConfigFile = File. createTempFile( "eToken", "cfg" );
        tmpConfigFile.deleteOnExit();

        PrintWriter configWriter = new PrintWriter( new FileOutputStream( tmpConfigFile ), true );

        configWriter.println( "name=eToken" );
        configWriter.println( "library=C:\\windows\\system32\\eTPKCS11.dll" );
        configWriter.println( "description = eToken config" );
        configWriter.println( "slot = 0" );

        System.out.println( tmpConfigFile.getAbsolutePath() );

        SunPKCS11 provider = new SunPKCS11( tmpConfigFile.getAbsolutePath() );
        Security.addProvider( provider );

        char[] pin = "dusrn2xla!@".toCharArray();

        KeyStore keyStoreP11 = KeyStore.getInstance( "PKCS11", provider );
        keyStoreP11.load( null, pin );

        Key key = KeyGenerator.getInstance( "AES", provider ).generateKey();

        PrivateKey priKey = (PrivateKey) keyStoreP11.getKey( "EE49E01E620374FC", "123".toCharArray() );
        X509Certificate cert = (X509Certificate) keyStoreP11.getCertificate( "EE49E01E620374FC" );

        Cipher cipher = Cipher.getInstance( "RSA", provider );

        cipher.init( Cipher.WRAP_MODE, cert.getPublicKey() );

        byte[] wrrapedKey = cipher.wrap( key );

        cipher.init( Cipher.UNWRAP_MODE, priKey );
        Key unWrrapedKey = cipher.unwrap( wrrapedKey, "RSA", Cipher.SECRET_KEY );


        System.out.println( Arrays.toString( key.getEncoded() ) );
        System.out.println( Arrays.toString( wrrapedKey ) );
        System.out.println( Arrays.toString( unWrrapedKey.getEncoded() ) );
    }
}
