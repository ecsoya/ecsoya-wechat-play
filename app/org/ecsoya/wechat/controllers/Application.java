package org.ecsoya.wechat.controllers;

import org.ecsoya.wechat.utils.EcsoyaWeChat;
import org.ecsoya.wechat.utils.SHA1Utils;

import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

public class Application extends Controller {

	public static Result index() {
		Request request = request();

		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");
		String echostr = request.getQueryString("echostr");

		if (timestamp != null && nonce != null) {
			try {
				String sign = SHA1Utils.getSHA1Sign(EcsoyaWeChat.TOKEN,
						timestamp, nonce);
				if (sign != null && sign.equals(signature)) {
					return ok(echostr);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return badRequest(e.getMessage());
			}
		}
		if (echostr != null) {
			return ok(echostr);
		}

		return ok("Invalid Token");
	}

}
