package http.apache;

import java.util.Map;

/**
 * <h1>HTTP Invoker. This Interface abstract the HTTP Client Module.</h1>
 *
 * @author      ehdvudee
 * @version     1.0.1
 * @since       2019-12-20
 */
public interface IInvoker {

    public String invokeHttp(String target, Map<String, String> headers, String httpMethod, String body );

}
