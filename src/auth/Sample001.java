package auth;

import org.junit.Test;

import java.lang.reflect.Method;

public class Sample001 {

    @Test
    @Auth( authState = {Auth.AuthState.ADMIN, Auth.AuthState.SUPER_ADMIN, Auth.AuthState.DEVEL_USER})
    public void test001() throws NoSuchMethodException {
        Method method = Sample001.class.getDeclaredMethod( "test001" );

        int myBitMask = Auth.AuthState.ADMIN.getAuthState();
        Auth auth = method.getAnnotation( Auth.class );

        System.out.println( isAuthorized( myBitMask, auth ) );
    }

    private boolean isAuthorized( int requestedBitMask, Auth auth ) {
        int bitMask = 0;

        for ( Auth.AuthState enumAuth : auth.authState() ) {
            bitMask = bitMask + enumAuth.getAuthState();
        }

        if ( ( requestedBitMask & bitMask ) == requestedBitMask ) return true;

        return false;
    }
}
