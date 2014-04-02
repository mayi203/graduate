/**
 * 
 */
package mayi.lagou.com;

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

}
