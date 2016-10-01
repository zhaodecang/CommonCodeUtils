package com.zdc.mobilesafe93.utils;

import java.security.MessageDigest;

/**
 * description:对文本进行MD5加密
 * 
 * @author zhaodecang
 * @date 2016-8-20上午11:33:39
 */
public class MD5Util {
	public static String encoder(String str) {
		str = str +"zdc_md5_encoder";// 加盐
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bs = digest.digest(str.getBytes("utf-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : bs) {
				int i = b & 0xff;
				String hexStr = Integer.toHexString(i);
				if (hexStr.length()<2) {
					hexStr = "0"+hexStr;
				}
				sb.append(hexStr);
			}
			return sb.toString();
		} catch (Exception e) {
			LogUtil.e("MD5Util", "MD5加密失败");
			e.printStackTrace();
		}
		return null;
	}
}
