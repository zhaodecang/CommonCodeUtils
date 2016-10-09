package com.zdc.googlemarket.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.zdc.googlemarket.global.MyApplication;

/**
 * @description 应用程序的工具类,包括获取handler、context、资源文件、像素 </br>加载一个布局、线程相关操作
 * 
 * @author zhaodecang
 * @date 2016-10-4下午2:03:11
 */
public class CommonUtil {

	public static Handler getHandler() {
		return MyApplication.getmHandler();
	}

	public static Context getContext() {
		return MyApplication.getmContext();
	}

	// -------------------线程相关-------------------
	/** 获取主线程id **/
	public static int getMainThreadId() {
		return MyApplication.getMainThreadId();
	}

	/** 判断当前线程是否运行在主线程 **/
	public static boolean isRunOnMainThread() {
		int currentThreadId = Process.myTid();
		return getMainThreadId() == currentThreadId;
	}

	/** 在主线程执行一个任务 **/
	public static void RunOnMainThread(Runnable runnable) {
		// 判断当前线程是否运行在主线程
		if (isRunOnMainThread()) {
			// 如果是 就直接执行任务
			runnable.run();
		} else {
			// 否则 通过handler传递到主线程运行
			getHandler().post(runnable);
		}
	}

	// -------------------加载资源文件-------------------
	/** 获取资源文件中的字符串 **/
	public static String getString(int id) {
		return getContext().getResources().getString(id);
	}

	/** 获取资源文件中的字符串数组 **/
	public static String[] getStringArray(int id) {
		return getContext().getResources().getStringArray(id);
	}

	/** 获取资源文件中的颜色值 **/
	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}

	/** 获取颜色的状态选择器 **/
	public static ColorStateList getColorStateList(int id) {
		return getContext().getResources().getColorStateList(id);
	}

	/** 获取资源文件中的图片资源 **/
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}

	/** 加载布局文件 **/
	public static View viewInflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	// -------------------像素和分辨率-------------------
	/** 获取像素比 **/
	public static float getDisplayDensity() {
		return getContext().getResources().getDisplayMetrics().density;
	}

	public static int dp2px(float dp) {
		return (int) (dp * getDisplayDensity() + 0.5f);
	}

	public static float px2dp(int px) {
		return px / getDisplayDensity();
	}
}
