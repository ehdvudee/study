package jce.zero;

import nocommit.JavaVersion;
import org.junit.Test;
import sun.security.pkcs.PKCS8Key;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DestroyUtil {

    @Test
    public void test001() throws NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
        SecretKey symmetricKey = KeyGenerator.getInstance( "AES" ).generateKey();

        System.out.println( "before zerorize SecretKey.class : " + DatatypeConverter.printHexBinary( symmetricKey.getEncoded() ) );
        zeroize( symmetricKey );
        System.out.println( "after zerorize SecretKey.class : " + DatatypeConverter.printHexBinary( symmetricKey.getEncoded() ) );


    }

    @Test
	public void test002() throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException {
		KeyPair kp = KeyPairGenerator.getInstance( "RSA" ).generateKeyPair();

		System.out.println( "before zerorize Privatekey.class : " + DatatypeConverter.printHexBinary( kp.getPrivate().getEncoded() ) );
		zeroize( kp.getPrivate() );
		System.out.println( "after zerorize Privatekey.class : " + DatatypeConverter.printHexBinary( kp.getPrivate().getEncoded() ) );
	}

	@Test
	public void test003() throws NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
		String hexKeyVal = DatatypeConverter.printHexBinary( KeyGenerator.getInstance( "AES" ).generateKey().getEncoded() );

		System.out.println( "before zerorize String.class : " +  hexKeyVal );
		zeroize( hexKeyVal );
		System.out.println( "after zerorize String.class : " + hexKeyVal );
	}

	@Test
	public void test004() throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException {
		// java.policy에 아래 사항 추가
    	// permission java.lang.RuntimePermission "accessClassInPackage.sun.security.*";
		// permission java.lang.RuntimePermission "accessDeclaredMembers";
		// 그러면 최종적으로 java.security.AccessControlException: access denied ("java.lang.reflect.ReflectPermission" "suppressAccessChecks")
		// 예외 메시지를 얻는다.
		System.setSecurityManager( new SecurityManager() );

		String hexKeyVal = DatatypeConverter.printHexBinary( KeyGenerator.getInstance( "AES" ).generateKey().getEncoded() );

		System.out.println( "before zerorize String.class : " +  hexKeyVal );
		zeroize( hexKeyVal );
		System.out.println( "after zerorize String.class : " + hexKeyVal );
	}
	public void zeroize( Object obj ) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		if( obj == null ) {
			throw new NullPointerException( "obj is null." );
		}

		String fieldNames[] = {};

		if( obj instanceof RSAPrivateCrtKeyImpl ) {
			fieldNames = new String[] { "n", "e", "d", "p", "q", "pe", "qe", "coeff", "key", "encodedKey" };
		}
		else if( obj instanceof PKCS8Key /*PrivateKey*/ ) {
			fieldNames = new String[] { "key", "encodedKey" };
		}
		else if( obj instanceof Key /*SecretKey => SecretKeySpec*/ ) {
			fieldNames = new String[] { "key" };
		}
		else if( obj instanceof IvParameterSpec ) {

			if( JavaVersion.current().ordinal() < JavaVersion.VERSION_1_7.ordinal() ) {
				fieldNames = new String[] { "a" };
			}
			else {
				fieldNames = new String[] { "iv" };
			}
		}
		else if( obj instanceof String ) {
			fieldNames = new String[] { "value" };
		}
		else if( obj instanceof BigInteger ) {
			fieldNames = new String[] { "mag" };
		} else {
            throw new IllegalArgumentException( obj.toString() + " is invalid. or CNA NOT ZEROIZE." );
        }

		for( String fieldName : fieldNames ) {
            Field field;

		    try {
                field = obj.getClass().getDeclaredField( fieldName );
            } catch ( NoSuchFieldException e ) {
                field = obj.getClass().getSuperclass().getDeclaredField( fieldName );
            }
            field.setAccessible( true );

	       	zerofill( field.get( obj ));
		}
	}

	public void zerofill( Object obj ) throws NoSuchFieldException, IllegalAccessException {

        if( obj instanceof byte[] ) {
        	Arrays.fill((byte[])obj, (byte)0 );
        }
        else if( obj instanceof char[] ) {
        	Arrays.fill((char[])obj, (char)0 );
        }
        else if( obj instanceof int[] ) {
        	Arrays.fill((int[])obj, 0 );
        } else if ( obj instanceof BigInteger ) {
            zeroize( obj );
        }
	}
}