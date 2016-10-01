package com.zdc.mobilesafe93.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void show(Context context, String str, int duration) {
		Toast.makeText(context, str, duration).show();
	}

	public static void show(Context context, String str) {
		Toast.makeText(context, str, 0).show();
	}
}
