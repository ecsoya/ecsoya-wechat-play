package org.ecsoya.wechat.auth;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.SimpleResult;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;

public class RequestAuthenticatorAction extends Action.Simple {

	static final String TOKEN = "EcsoyaWeChat";

	public Promise<SimpleResult> call(Context context) throws Throwable {
		Request request = context.request();
		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");
		String encrypt = request.getQueryString("encrypt");

		if (timestamp != null && nonce != null) {
			try {
				String sign = SHA1.getSHA1(TOKEN, timestamp, nonce, encrypt);
				if (sign != null && sign.equals(signature)) {
					return Promise.pure(ok());
				}
			} catch (AesException e) {
				return Promise.pure((SimpleResult) badRequest("Bad request: "
						+ e.getMessage()));
			}
		}
		return Promise.pure(badRequest("no tokens found"));
	}

}
