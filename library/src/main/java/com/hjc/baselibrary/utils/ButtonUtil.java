package com.hjc.baselibrary.utils;


import android.os.Handler;
import android.os.Looper;

/**
 * 按纽点击控制方法
 */
public class ButtonUtil {
	private static final long DIFF = 2000;

	private static int lastButtonId = -1;
	private static boolean isQuickClick = false;

	private static Handler handler = new Handler(Looper.getMainLooper());

	private static Runnable delayRunnable = new Runnable() {
		@Override
		public void run() {
			isQuickClick = false;
		}
	};

	private ButtonUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("ButtonUtil cannot be instantiated");
	}

	/**
	 * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		return isFastDoubleClick(-1, DIFF);
	}

	/**
	 * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick(int buttonId) {
		return isFastDoubleClick(buttonId, DIFF);
	}

	/**
	 * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
	 *
	 * @param diff
	 * @return
	 */
	public static boolean isFastDoubleClick(int buttonId, long diff) {
		if (lastButtonId == buttonId && isQuickClick) {
			return true;
		}

		isQuickClick = true;
		lastButtonId = buttonId;

		handler.removeCallbacks(delayRunnable);
		handler.postDelayed(delayRunnable, diff);

		return false;
	}
}
