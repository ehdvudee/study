package jce;

import org.junit.Test;
import sun.security.rsa.RSASignature;
import sun.security.x509.AlgorithmId;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.*;

public class Sample024HashedDataSign {

    @Test
    public void sample001() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, IOException, BadPaddingException, SignatureException, InvalidKeyException {
        KeyPair keyPair = KeyPairGenerator.getInstance( "RSA" ).generateKeyPair();
        //==============================================
        // Sign Original Data
        //==============================================
        Signature signature = Signature.getInstance( "SHA256WithRSA" );
        signature.initSign( keyPair.getPrivate() );
        signature.update( "aa".getBytes() );
        byte[] ret1 = signature.sign();

        System.out.println( DatatypeConverter.printHexBinary( ret1 ) );

        signature.initVerify( keyPair.getPublic() );
        signature.update( "aa".getBytes() );
        System.out.println( signature.verify( ret1 ) );

        //==============================================
        // Sign Digested Data
        //==============================================
        MessageDigest md = MessageDigest.getInstance( "SHA-256" );
        byte[] hashed = md.digest( "aa".getBytes() );
        AlgorithmId algoId = AlgorithmId.get( "SHA-256" );
        System.out.println( algoId.getOID() );
        byte[] signedData = RSASignature.encodeSignature( algoId.getOID(), hashed );

        Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
        cipher.init( Cipher.ENCRYPT_MODE, keyPair.getPrivate() );
        cipher.update( signedData );
        byte[] ret2 = cipher.doFinal();

        if ( DatatypeConverter.printHexBinary( ret1 ).equals( DatatypeConverter.printHexBinary( ret2 ) ) ) {
            System.out.println( "성공" );
        } else {
            System.out.println( "실패" );
        }

        System.out.println( DatatypeConverter.printHexBinary( ret2 ) );

        cipher.init( Cipher.DECRYPT_MODE, keyPair.getPublic() );
        cipher.update( ret2 );
        byte[] ret3 = cipher.doFinal();

        System.out.println( DatatypeConverter.printHexBinary( ret3) );
        System.out.println( DatatypeConverter.printHexBinary( signedData ) );
    }

}
