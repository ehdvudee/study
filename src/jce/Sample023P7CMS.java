package jce;

import org.junit.Test;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.pkcs12.PKCS12KeyStore;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Sample023P7CMS {

    private final String PW = "123";

    @Test
    public void sample001() throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, SignatureException {
        File userP12 = new File( "./p12/dev.p12");

        PKCS12KeyStore pkcs12kStr = new PKCS12KeyStore();

        try ( FileInputStream is = new FileInputStream( userP12 ) ) {
            pkcs12kStr.engineLoad( is, PW.toCharArray() );
        }

        Certificate[] chain = pkcs12kStr.engineGetCertificateChain( "dev" );
        X509Certificate cert = (X509Certificate) chain[0];
        Key priKey = pkcs12kStr.engineGetKey( "dev", PW.toCharArray() );
        byte[] data = "datayo".getBytes();

        Signature signature = Signature.getInstance( "SHA256WithRSA" );
        signature.initSign( (PrivateKey)priKey );
        signature.update( data );
        byte[] signedData = signature.sign();

        X500Name xName = X500Name.asX500Name( cert.getSubjectX500Principal() );
        BigInteger serial = cert.getSerialNumber();
        AlgorithmId digestAlgorithmId = new AlgorithmId( AlgorithmId.SHA256_oid );
        AlgorithmId signAlgorithmId = new AlgorithmId( AlgorithmId.sha256WithRSAEncryption_oid );

        SignerInfo sInfo = new SignerInfo( xName, serial, digestAlgorithmId, signAlgorithmId, signedData );
        ContentInfo cInfo = new ContentInfo( ContentInfo.DIGESTED_DATA_OID, new DerValue(DerValue.tag_OctetString, data ));

        PKCS7 pkcs7 = new PKCS7(
                new AlgorithmId[] { digestAlgorithmId },
                cInfo, new X509Certificate[] { cert },
                new SignerInfo[] { sInfo } );

        ByteArrayOutputStream bOut = new DerOutputStream();
        pkcs7.encodeSignedData(bOut);

        byte[] encodedPKCS7 = bOut.toByteArray();

        System.out.println( DatatypeConverter.printHexBinary(encodedPKCS7 ) );

        File userP7 = new File( "./p7/dev.p7");
        try ( FileOutputStream os = new FileOutputStream( userP7 ) ) {
            os.write( encodedPKCS7 );
        }
    }
}
