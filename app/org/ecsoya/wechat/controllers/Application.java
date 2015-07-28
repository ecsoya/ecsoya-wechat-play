package org.ecsoya.wechat.controllers;

import java.util.Map;

import org.ecsoya.wechat.SHA1;
import org.ecsoya.wechat.utils.JsonUtils;

import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

	static final String APP_ID = "wx37ee0c4f61cca002";
	static final String TOKEN = "EcsoyaWeChat";
	static final String ENCODING_AES_KEY = "UvSfE2khr5yNxAkVAtcuzFqC0iOAj4OVxg0zXzB3HIu";

	public static Result index() {
		Request request = request();

		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString( "nonce");
		String echostr = request.getQueryString("echostr");

		if (timestamp != null && nonce != null) {
			String sign = SHA1.checkSignature(TOKEN, timestamp, nonce);
			if (sign != null && sign.equals(signature)) {
				return ok(echostr);
			}
		}

		return ok("Invalid Token");
	}

}
