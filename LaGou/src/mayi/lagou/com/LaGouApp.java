/**
 * 
 */
package mayi.lagou.com;

import java.lang.reflect.Field;

import mayi.lagou.com.imageloader.ImageLoader;
import mayi.lagou.com.utils.SharePreferenceUtil;
import android.app.Activity;
import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;
import com.umeng.fb.FeedbackAgent;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class LaGouApp extends Application {

	private static LaGouApp _instance;
	private ImageLoader imageLoader;
	public String mSdcardDataDir;
	public static boolean isLogin = false;
	public AsyncHttpClient client;

	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		client = new AsyncHttpClient();
		imageLoader = new ImageLoader(this, 0.3f);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		mSdcardDataDir = getExternalCacheDir().getPath();
		isLogin = SharePreferenceUtil.getBoolean(this, "islogin");
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
	//获取状态栏高度
	public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
