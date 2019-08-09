package jce.cert.sample;

import sun.security.util.ObjectIdentifier;
import sun.security.x509.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Vector;

public class TestDrive001 {

    public static final String TEST_CN = "issuerTest";

    /**
     * Create a self-signed X.509 Certificate
     * @param dn the X.509 Distinguished Name, eg "CN=Test, L=London, C=GB"
     * @param pair the KeyPair
     * @param days how many days from now the Certificate is valid for
     * @param algorithm the signing algorithm, eg "SHA1withRSA"
     */
    X509Certificate generateCertificate( String dn, KeyPair pair, int days, String algorithm)
            throws GeneralSecurityException, IOException
    {
        PrivateKey privkey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + days * 86400000l);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name owner = new X500Name(dn);

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
//        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
//        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, owner);
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));




        KeyUsageExtension keyUsage = new KeyUsageExtension();
        keyUsage.set( KeyUsageExtension.KEY_CERTSIGN, true );

        CertificateExtensions exts = new CertificateExtensions();

        GeneralNames issuerAltName = new GeneralNames();
        issuerAltName.add( new GeneralName( new X500Name( "CN = " + TEST_CN ) ) );
        exts.set( IssuerAlternativeNameExtension.NAME,
                new IssuerAlternativeNameExtension( false, issuerAltName ) );

        GeneralNames subjectAltName = new GeneralNames();
        subjectAltName.add( new GeneralName( new X500Name( "CN = " + TEST_CN ) ) );
        exts.set( SubjectAlternativeNameExtension.NAME,
                new SubjectAlternativeNameExtension( false, subjectAltName ) );

        exts.set( SubjectKeyIdentifierExtension.NAME,
                new SubjectKeyIdentifierExtension(
                        new KeyIdentifier( pair.getPublic() ).getIdentifier() ) );

        exts.set( BasicConstraintsExtension.NAME,
                new BasicConstraintsExtension( true, -1 ) );

        exts.set( KeyUsageExtension.NAME, keyUsage );

        info.set( X509CertInfo.EXTENSIONS, exts );







        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        return cert;
    }

    X509Certificate generateCertificate( X509Certificate issuerCert, String issuerDn, String subjectDn, KeyPair issuerKeyPair, KeyPair subjectKeyPair,  int days, String algorithm)
            throws GeneralSecurityException, IOException
    {
        PrivateKey privkey = issuerKeyPair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + days * 86400000l);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name issuer = new X500Name( issuerDn );
        X500Name subject  = new X500Name( subjectDn );



        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, issuer);
        info.set(X509CertInfo.ISSUER, subject);
        info.set(X509CertInfo.KEY, new CertificateX509Key(subjectKeyPair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));



        KeyUsageExtension keyUsage = new KeyUsageExtension();

        keyUsage.set( KeyUsageExtension.KEY_CERTSIGN, true );
//		keyUsage.set( KeyUsageExtension.CRL_SIGN, true );

        Vector<ObjectIdentifier> keyOid = new Vector<ObjectIdentifier>();
        keyOid.add( new ObjectIdentifier( new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 1 }));	// server authentication
        keyOid.add( new ObjectIdentifier( new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 2 }));	// client authentication

        CertificateExtensions exts = new CertificateExtensions();

        exts.set( SubjectKeyIdentifierExtension.NAME,
                new SubjectKeyIdentifierExtension(
                        new KeyIdentifier( subjectKeyPair.getPublic() ).getIdentifier() ) );

        exts.set( KeyUsageExtension.NAME, keyUsage );

        exts.set( ExtendedKeyUsageExtension.NAME,
                new ExtendedKeyUsageExtension( keyOid ));

        exts.set( BasicConstraintsExtension.NAME,
                new BasicConstraintsExtension( true, 0 ) );

        exts.set( AuthorityKeyIdentifierExtension.NAME,
                new AuthorityKeyIdentifierExtension(
                        new KeyIdentifier( issuerCert.getPublicKey()),
                        new GeneralNames().add( new GeneralName(
                                new X500Name( issuerCert.getSubjectDN().getName() ) ) ),
                        new SerialNumber( issuerCert.getSerialNumber())
                ));

        info.set( X509CertInfo.EXTENSIONS, exts );




        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        return cert;
    }

    public static void main (String[] argv) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize( 2048 );

        KeyPair issuerKeyPair = keyPairGenerator.generateKeyPair();
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair();

        TestDrive001 example = new TestDrive001();

        String issuerDn = "CN=issuerTest, L=London, C=GB";
        String subjectDn = "CN=subjectTest2, L=Seoul, C=GB";

        X509Certificate issuerCert = example.generateCertificate( issuerDn, issuerKeyPair, 365, "SHA256withRSA");
        X509Certificate subjectCert = example.generateCertificate( issuerCert, issuerDn, subjectDn, issuerKeyPair, subjectKeyPair, 365, "SHA256withRSA");

        System.out.println("it worked!");


        FileOutputStream localFileOutputStream = null;
        try {
            localFileOutputStream = new FileOutputStream("./doc/issuerCert.der");
            localFileOutputStream.write( issuerCert.getEncoded());
            localFileOutputStream.close();

            localFileOutputStream = new FileOutputStream("./doc/subjectCert.der");
            localFileOutputStream.write( subjectCert.getEncoded());
            localFileOutputStream.close();
        } finally {
            if (localFileOutputStream != null) localFileOutputStream.close();
        }
    }
}
