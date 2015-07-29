package org.ecsoya.wechat.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;

/**
 * Marks a controller method as requiring authentication. This annotation will
 * ensure validation is performed for that request. On authentication failure
 * the request will be denied with a suitable Http status code.
 */
@With(RequestAuthenticatorAction.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticateRequest {

}
