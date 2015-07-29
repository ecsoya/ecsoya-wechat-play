package org.ecsoya.wechat.controllers;

import org.ecsoya.wechat.auth.AuthenticateRequest;
import org.ecsoya.wechat.utils.EcsoyaWeChat;

import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import com.qq.weixin.mp.aes.AesException;

public class Application extends Controller {

	@AuthenticateRequest
	public static Result index() {
		Request request = request();
		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");
		String echostr = request.getQueryString("echostr");
		if (echostr != null) {
			try {
				return ok(EcsoyaWeChat.getCrypt().verifyUrl(signature,
						timestamp, nonce, echostr));
			} catch (AesException e) {
				return badRequest(e.getMessage());
			}
		}
		return ok("Invalid Token");
	}

}
