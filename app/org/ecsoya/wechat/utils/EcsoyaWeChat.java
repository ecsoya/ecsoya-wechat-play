/**
 * 
 */
package org.ecsoya.wechat.utils;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class EcsoyaWeChat {
	public static final String TOKEN = "EcsoyaWeChat";
	public static final String APP_ID = "wx37ee0c4f61cca002";
	public static final String ENCODING_AES_KEY = "UvSfE2khr5yNxAkVAtcuzFqC0iOAj4OVxg0zXzB3HIu";

	private static WXBizMsgCrypt crypt;

	public static WXBizMsgCrypt getCrypt() throws AesException {
		if (crypt == null) {
			crypt = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, APP_ID);
		}
		return crypt;
	}

}
