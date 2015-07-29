package org.ecsoya.wechat.controllers;

import org.ecsoya.wechat.auth.AuthenticateRequest;

import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

public class Application extends Controller {

	static final String APP_ID = "wx37ee0c4f61cca002";
	static final String ENCODING_AES_KEY = "UvSfE2khr5yNxAkVAtcuzFqC0iOAj4OVxg0zXzB3HIu";

	@AuthenticateRequest
	public static Result index() {
		Request request = request();

		String echostr = request.getQueryString("echostr");
		if (echostr != null) {
			return ok(echostr);
		}
		return ok("Invalid Token");
	}

}
