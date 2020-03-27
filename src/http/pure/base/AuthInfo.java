package http.pure.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME )
public @interface AuthInfo {
	public String id() default "admin";
	public String pw() default "asdasd";
}
