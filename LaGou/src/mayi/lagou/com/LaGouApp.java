/**
 * 
 */
package mayi.lagou.com;

import mayi.lagou.com.imageloader.ImageLoader;
import android.app.Activity;
import android.app.Application;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class LaGouApp extends Application {

	private static LaGouApp _instance;
	private ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		imageLoader = new ImageLoader(this, 0.3f);
	}

	public static LaGouApp getInstance() {
		if (_instance == null) {
			_instance = new LaGouApp();
		}
		return _instance;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	private static int screenWidth = 0;

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Activity activity) {
		if (screenWidth == 0) {
			screenWidth = activity.getWindow().getWindowManager()
					.getDefaultDisplay().getWidth();
		}
		return screenWidth;
	}

	private static int screenHeight = 0;

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Activity activity) {
		if (screenHeight == 0) {
			screenHeight = activity.getWindow().getWindowManager()
					.getDefaultDisplay().getHeight();
		}
		return screenHeight;
	}
}
