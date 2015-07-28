package org.ecsoya.wechat.controllers;

import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;

public class Application extends Controller {

	static final String APP_ID = "wx37ee0c4f61cca002";
	static final String TOKEN = "EcsoyaWeChat";
	static final String ENCODING_AES_KEY = "UvSfE2khr5yNxAkVAtcuzFqC0iOAj4OVxg0zXzB3HIu";

	public static Result index() {
		Request request = request();

		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");
		String encrypt = request.getQueryString("encrypt");
		String echostr = request.getQueryString("echostr");

		if (timestamp != null && nonce != null) {
			try {
				String sign = SHA1.getSHA1(TOKEN, timestamp, nonce, encrypt);
				if (sign != null && sign.equals(signature)) {
					return ok(echostr);
				}
			} catch (AesException e) {
				badRequest(e.getMessage());
			}
		}

		return ok("Invalid Token");
	}

}
