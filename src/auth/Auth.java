package auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target(ElementType.METHOD)
public @interface Auth {
    public AuthState[] authState() default AuthState.ADMIN;

    public enum AuthState {
        SUPER_ADMIN( 1 ),
        ADMIN( 2 ),
        DEVEL_USER( 4 ),
        COMMON_USER( 8 ),
        NON_USER( 16 );

        private int authState;

        AuthState( int arg ) {
            this.authState = arg;
        }

        public int getAuthState() {
            return this.authState;
        }
    }
}
