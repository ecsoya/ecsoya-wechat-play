package org.ecsoya.wechat.auth;

import org.ecsoya.wechat.utils.EcsoyaWeChat;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.SimpleResult;

import com.qq.weixin.mp.aes.SHA1;

public class RequestAuthenticatorAction extends Action.Simple {

	public Promise<SimpleResult> call(Context context) throws Throwable {
		Request request = context.request();
		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");
		String echostr = request.getQueryString("echostr");
		if (signature == null || timestamp == null || nonce == null) {
			return Promise.pure((SimpleResult) ok(echostr));
		}
		String sha1 = SHA1.getSHA1(EcsoyaWeChat.TOKEN, timestamp, nonce,
				echostr);
		if (signature != null && !signature.equals(sha1)) {
			return Promise
					.pure((SimpleResult) badRequest("Anthenticate failed: "
							+ signature));
		}
		return delegate.call(context);
	}
}
