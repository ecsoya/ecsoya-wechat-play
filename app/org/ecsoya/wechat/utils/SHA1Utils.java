/**
 * 
 */
package org.ecsoya.wechat.utils;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class SHA1Utils {

	public static String getSHA1Sign(String token, String timestamp,
			String nonce) throws Exception {

		String[] paramArry = { token, timestamp, nonce };

		Arrays.sort(paramArry);

		String str = paramArry[0].concat(paramArry[1]).concat(paramArry[2]);

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			throw e;
		}

		byte[] digest = md.digest(str.getBytes());

		String digestResult = byte2Str(digest);

		return digestResult.toLowerCase();
	}

	private static String byte2Str(byte[] digest) {

		String strDigest = "";

		for (int i = 0; i < digest.length; i++) {

			strDigest += byte2HexStr(digest[i]);

		}

		return strDigest;
	}

	private static String byte2HexStr(byte b) {

		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };

		char[] tempArry = new char[2];

		tempArry[0] = digit[(b >>> 4) & 0X0F];
		tempArry[1] = digit[b & 0X0F];

		return new String(tempArry);
	}
}
