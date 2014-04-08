/**
 * 
 */
package mayi.lagou.com;

import android.app.Activity;
import android.app.Application;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class LaGouApp extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	private static LaGouApp _instance;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
	}

	public static LaGouApp getInstance() {
		if (_instance == null) {
			_instance = new LaGouApp();
		}
		return _instance;
	}
	private static int screenWidth = 0;

	public static int getScreenWidth(Activity activity) {
		if (screenWidth == 0) {
			screenWidth = activity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		}
		return screenWidth;
	}

	private static int screenHeight = 0;

	public static int getScreenHeight(Activity activity) {
		if (screenHeight == 0) {
			screenHeight = activity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
		}
		return screenHeight;
	}
}
