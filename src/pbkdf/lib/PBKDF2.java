package pbkdf.lib;

public interface PBKDF2 {

	public abstract byte[] deriveKey( byte[] P, byte[] salt, int iterationCount, int derivedKeyLength );
}