package com.zdc.netconnect;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * description:检查网络连接的工具
 * 
 * @author zhaodecang
 * @date 2016-9-21下午12:46:48
 */
public class NetUtil {

	private static ConnectivityManager manager;

	/**
	 * 获取ConnectivityManager
	 * 
	 * @return ConnectivityManager
	 * 
	 * @权限 ACCESS_NETWORK_STATE
	 */
	private static ConnectivityManager getCm(Context context) {
		if (manager == null) {
			manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return manager;
	}

	/**
	 * 1、检查是否是wifi连接网络
	 * 
	 * @return true wifi连接;false 无网络或者不是wifi连接
	 */
	public static boolean isWifiConnected(Context context) {
		NetworkInfo networkInfo = getCm(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}

	/**
	 * 2、检查是否是手机连接（wap、net）
	 * 
	 * @return true 手机连接;false 无网络或者不是手机连接
	 */
	public static boolean isMobileConnected(Context context) {
		NetworkInfo networkInfo = getCm(context).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}

	/**
	 * 4、检查是否有网络连接(wifi，手机)
	 * 
	 * @return true 有网络连接(WiFi或者手机连接);false 没有网络连接
	 */
	public static boolean checkNetwork(Context context) {
		// 1、wifi连接
		if (isWifiConnected(context)) {
			return true;
		}
		// 2、手机连接
		if (isMobileConnected(context)) {
			return true;
		}
		// 3、当前没有连接网络
		return false;
	}

	/**
	 * 5、检查手机连接是wap还是net
	 * 
	 * @return null 表示无网络或者wifi连接中;其他则为wap/net连接
	 */
	public static String isWap(Context context) {
		if (!isMobileConnected(context)) {
			return null;
		}
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://telephony/carriers/preferapn");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.getCount() == 1) {
			if (cursor.moveToNext()) {
				// 代理ip
				String proxy_ip = cursor.getString(cursor.getColumnIndex("proxy"));
				// 代理端口
				String proxy_port = cursor.getString(cursor.getColumnIndex("port"));
				if (!TextUtils.isEmpty(proxy_ip) && !TextUtils.isEmpty(proxy_port)) {
					return "wap";
				}
			}
		}
		cursor.close();
		return "net";
	}
}