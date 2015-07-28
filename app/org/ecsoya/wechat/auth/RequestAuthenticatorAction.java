package org.ecsoya.wechat.auth;

import org.ecsoya.wechat.utils.SHA1Utils;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.SimpleResult;

public class RequestAuthenticatorAction extends Action.Simple {

	static final String TOKEN = "EcsoyaWeChat";

	public Promise<SimpleResult> call(Context context) throws Throwable {
		Request request = context.request();
		String signature = request.getQueryString("signature");
		String timestamp = request.getQueryString("timestamp");
		String nonce = request.getQueryString("nonce");

		if (timestamp != null && nonce != null) {
			try {
				String sign = SHA1Utils.getSHA1Sign(TOKEN, timestamp, nonce);
				if (sign != null && sign.equals(signature)) {
					return Promise.pure((SimpleResult) ok());
				}
			} catch (Exception e) {
				return Promise.pure((SimpleResult) badRequest("Bad request: "
						+ e.getMessage()));
			}
		}
		return Promise.pure((SimpleResult) badRequest("no tokens found"));
	}

}
