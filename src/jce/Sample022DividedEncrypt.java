package jce;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Sample022DividedEncrypt {

    @Test
    public void smpale001() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {

        byte[] plaintext = DatatypeConverter.parseHexBinary( "a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a2a2a2a2a2" );

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance( "RSA" );
        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 1024;
        keyGenerator.initialize( keyBitSize, secureRandom );

        KeyPair keypair = keyGenerator.generateKeyPair();

        Cipher cipher = Cipher.getInstance( "RSA" );
        cipher.init( Cipher.ENCRYPT_MODE, keypair.getPublic() );

        byte[] ciphertext = dividedDoFinal( plaintext, cipher, Cipher.ENCRYPT_MODE, keyBitSize );

        cipher.init( Cipher.DECRYPT_MODE, keypair.getPrivate() );
        System.out.println( DatatypeConverter.printHexBinary( dividedDoFinal( ciphertext, cipher, Cipher.DECRYPT_MODE, keyBitSize ) ) );

    }

    public byte[] dividedDoFinal( byte[] plaintext, Cipher cipher, int cryptoMode, int keyLen ) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int enabledEncryptSize;
        if ( cryptoMode == Cipher.ENCRYPT_MODE ) {
            enabledEncryptSize = keyLen / 8 - 11;
        } else if ( cryptoMode == Cipher.DECRYPT_MODE ) {
            enabledEncryptSize = keyLen / 8;
        } else {
            throw new IllegalArgumentException( "CRYPTO_MODE is invalid." );
        }
        List<byte[]> plaintextList = sliceByteArray( plaintext, enabledEncryptSize );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        for ( byte[] plain : plaintextList ) {
            bos.write( cipher.doFinal( plain ) );
        }

        return bos.toByteArray();
    }

    public List<byte[]> sliceByteArray( byte[] bytea, int sliceCnt ) {
        List<byte[]> returnList = new ArrayList<byte[]>();
        int idx = 0;
        int whileCnt = bytea.length % sliceCnt == 0 ? bytea.length / sliceCnt : bytea.length / sliceCnt + 1;

        for ( int i=0; i<whileCnt; i++ ) {
            int cutCnt = bytea.length - idx >= sliceCnt ? sliceCnt : bytea.length - idx;
            byte[] tmpBytea = new byte[cutCnt];

            System.arraycopy( bytea, idx, tmpBytea, 0, cutCnt );

            idx = idx + sliceCnt;
            returnList.add( tmpBytea );
        }

        return returnList;
    }

    public byte[] concatByteArrays(byte[]... bytes ) {
        int i = 0;

        for ( byte[] b : bytes ) {
            i = i + b.length;
        }

        byte[] concatedBytes = new byte[i];
        i = 0;

        for ( byte[] b : bytes ) {
            System.arraycopy( b, 0, concatedBytes, i, b.length );
            i = i + b.length;
        }

        return concatedBytes;
    }
}
