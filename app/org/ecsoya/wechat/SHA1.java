/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package org.ecsoya.wechat;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
public class SHA1 {

	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param encrypt 密文
	 * @return 安全签名
	 * @throws AesException 
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException
			  {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
	}
	
	/**
	 * 验证URL是否来自微信服务器
	 * 
	 *  按照微信接入指南，验证URL的方式：
	 *   
	 *   1. 将token、timestamp、nonce三个参数进行字典序排序
	 *   
	 *   2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 *   
	 *   3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * 
	 * @param req
	 * @return
	 */
	public static String checkSignature(String token, String timestamp, String nonce){
		
		String[] paramArry = {token, timestamp, nonce};
		
		Arrays.sort(paramArry);
		
		String str = paramArry[0].concat(paramArry[1]).concat(paramArry[2]);
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] digest = md.digest(str.getBytes());
		
		String digestResult = byte2Str(digest);
		
		return digestResult.toLowerCase() ;
	}

	private static String byte2Str(byte[] digest) {

		String strDigest = "";
		
		for(int i = 0; i < digest.length; i++){
			
			strDigest += byte2HexStr(digest[i]);
			
		}
		
		return strDigest;
	}

	private static String byte2HexStr(byte b) {

		char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		
		char[] tempArry = new char[2];
		
		tempArry[0] = digit[(b >>> 4) & 0X0F];
		tempArry[1] = digit[b & 0X0F];
		
		return new String(tempArry);
	}
}
