package mayi.lagou.com.utils;

import java.io.File;

import mayi.lagou.com.LaGouApp;
import android.content.Context;
import android.util.Log;

public class ConfigCache {
	private static final String TAG = ConfigCache.class.getName();
	public static final int CONFIG_CACHE_MOBILE_TIMEOUT = 3600000; // 1 hour
	public static final int CONFIG_CACHE_WIFI_TIMEOUT = 300000; // 5 minute

	public static String getUrlCache(String url, Context context) {
		if (url == null) {
			return null;
		}

		String result = null;
		File file = new File(LaGouApp.getInstance().mSdcardDataDir + "/"
				+ getCacheDecodeString(url));
		if (file.exists() && file.isFile()) {
			long expiredTime = System.currentTimeMillis() - file.lastModified();
			Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime
					/ 60000 + "min");
			// 1. in case the system time is incorrect (the time is turn back
			// long ago)
			// 2. when the network is invalid, you can only read the cache
			if (NetWorkState.isNetWorkConnected(context) && expiredTime < 0) {
				return null;
			}
			if (NetWorkState.isWifiConnected(context)
					&& expiredTime > CONFIG_CACHE_WIFI_TIMEOUT) {
				return null;
			} else if (NetWorkState.isNetWorkConnected(context)
					&& !NetWorkState.isWifiConnected(context)
					&& expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT) {
				return null;
			}
			try {
				result = FileUtils.readTextFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void setUrlCache(String data, String url) {
		File file = new File(LaGouApp.getInstance().mSdcardDataDir + "/"
				+ getCacheDecodeString(url));
		try {
			// 创建缓存数据到磁盘，就是创建文件
			FileUtils.writeTextFile(file, data);
		} catch (Exception e) {
			Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!");
			e.printStackTrace();
		}
	}

	public static String getCacheDecodeString(String url) {
		// 1. 处理特殊字符
		// 2. 去除后缀名带来的文件浏览器的视图凌乱(特别是图片更需要如此类似处理，否则有的手机打开图库，全是我们的缓存图片)
		if (url != null) {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
		return null;
	}
}
