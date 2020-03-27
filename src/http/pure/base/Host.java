package http.pure.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME )
public @interface Host {
	public String host() default "http://localhost";
	public String port() default "80";
}
