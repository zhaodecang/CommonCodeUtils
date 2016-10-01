package com.zdc.netconnect;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * description:高效地加载大图
 * 
 * @author zhaodecang
 * @date 2016-9-21下午3:42:59
 */
public class BitmapLoadUtil {
	/**
	 * 屏幕宽度
	 */
	private static int mScreen_width;
	/**
	 * 屏幕高度
	 */
	private static int mScreen_height;

	/**
	 * 初始化屏幕尺寸大小
	 * 
	 * @param context
	 */
	private static void initScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = wm.getDefaultDisplay();
		Point outSize = new Point();
		defaultDisplay.getSize(outSize);
		mScreen_width = outSize.x;
		mScreen_height = outSize.y;
	}

	/**
	 * 
	 * 计算缩放比
	 * 
	 * @param context
	 * @param img_width
	 * @param img_height
	 * @return
	 */
	private static int calculaeScale(Context context, int img_width, int img_height) {
		// 初始化
		initScreenSize(context);
		int inSampleSize = 1;// 默认缩放比
		if (img_width > mScreen_width || img_height > mScreen_height) {
			int scale_x = Math.round((float) img_width / mScreen_width);
			int scale_y = Math.round((float) img_height / mScreen_height);
			inSampleSize = Math.max(scale_x, scale_y);
		}
		return inSampleSize;
	}

	/**
	 * 通过计算出一个合适的缩放比加载图片(整体效率最优)
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap loadBitmapFine(Context context, int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 只读取图片属性 避免内存分配
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, opts);
		// 计算合适的缩放比
		int img_width = opts.outWidth;
		int img_height = opts.outHeight;
		int inSampleSize = calculaeScale(context, img_width, img_height);
		// 设置缩放比
		opts.inSampleSize = inSampleSize;
		Log.i("BitmapLoadUtil", "计算出来的缩放比为:" + inSampleSize);
		// 正真加载图片
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(context.getResources(), resId, opts);
	}

	/**
	 * 来自网络:以最省内存的方式读取本地资源的图片(节约时间,加载的是原图)
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 尝试加载,不断尝试最佳缩放比(这种方式容易导致oom)
	 * 
	 * @param resId
	 * @return
	 */
	public static Bitmap tryLoadBitmap(Context context, int resId) {
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		int i = 1;
		for (;;) {
			try {
				opts.inSampleSize = i;
				// 如果当前的压缩比例能够加载图片循环就会退出
				bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opts);
				Log.i("BitmapLoadUtil", "尝试加载成功,缩放比为:" + i);
				break;
			} catch (Error e) {
				// 如果走到异常了就提高压缩比例 继续试验
				// inSampleSize 这个值要取2的幂指数 如果不是2的N次幂 系统也会把这个数转化为最近2的N次幂的值
				i *= 2;
			}
		}
		return bitmap;
	}
}
