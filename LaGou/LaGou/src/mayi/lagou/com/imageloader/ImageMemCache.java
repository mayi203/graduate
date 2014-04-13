package mayi.lagou.com.imageloader;

import java.util.Map;

import mayi.lagou.com.utils.DevUtil;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

public class ImageMemCache {

	// Default memory cache size
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB

	// memory cache size
	private int memCacheSize = DEFAULT_MEM_CACHE_SIZE;

	// 缓存
	private LruCache<String, Bitmap> mMemoryCache;

	// 对外提供用于处理缓存回收时的自定义动作
	private ImageMemCacheEvent mImageMemCacheEvent;

	/**
	 * 
	 * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion 否则使用将不显示加载图片
	 * 
	 * 百分比方式设置缓存大小
	 * 
	 * @param context
	 * @param percent
	 *            Sets the memory cache size based on a percentage of the device memory class percent is < 0.05 or > .8. 参见：setMemCacheSizePercent方法
	 */
	public ImageMemCache(Context context, float percent) {
		setMemCacheSizePercent(context, percent);
		initCache();
	}

	/**
	 * 对外提供用于处理缓存回收时的自定义动作
	 * 
	 * @param imageMemCacheEvent
	 */
	public void setImageMemCacheEvent(ImageMemCacheEvent imageMemCacheEvent) {
		mImageMemCacheEvent = imageMemCacheEvent;
	}

	/**
	 * 设置缓存固定值大小
	 * 
	 * @param size
	 *            单位：字节
	 */
	public ImageMemCache(int size) {
		memCacheSize = size;
		initCache();
	}

	public Bitmap get(String key) {
		return mMemoryCache.get(key);
	}

	public void put(String key, Bitmap value) {
		DevUtil.v("jackzhou", String.format("mImageMemCache put:%s", value));

		// 不存在才添加。 否则会有问题：LruCache中有机制相同key会将老的踢掉，在图片中会造成相同url图片会将老的bitmap
		// recycle掉，无法显示
		if (!mMemoryCache.isCached(key)) {
			mMemoryCache.put(key, value);
		}

	}

	/**
	 * 更改缓存大小 在调用put方法时，会自动变化到新的大小。
	 * 
	 * @param maxSize
	 *            缓存大小，单位：字节
	 */
	public void setCacheSize(int maxSize) {
		mMemoryCache.setMaxSize(maxSize);
	}

	/**
	 * 清空缓存，所有图片会被recycle. 已作处理，可放心使用，recycle的图片不会报：trying to use a recycled bitmap错误
	 * 
	 */
	public void clearCache() {
		mMemoryCache.evictAll();
	}

	/**
	 * Returns a copy of the current contents of the cache, ordered from least recently accessed to most recently accessed.
	 */
	public Map<String, Bitmap> snapshot() {
		return mMemoryCache.snapshot();
	}

	/**
	 * Removes the entry for {@code key} if it exists.
	 * 
	 * @return the previous value mapped by {@code key}.
	 */
	public Bitmap remove(String key) {
		return mMemoryCache.remove(key);
	}

	public int hitCount() {
		return mMemoryCache.hitCount();
	}

	public int missCount() {
		return mMemoryCache.missCount();
	}

	public int maxSize() {
		return mMemoryCache.maxSize();
	}

	public int size() {
		return mMemoryCache.size();
	}

	private void initCache() {

		// LruCache<String, Bitmap>(memCacheSize) LruCache中限制传入不能为0 会异常
		// 这里设置个最小值1，1字节的缓存对于图片来说等于无缓存
		if (memCacheSize == 0) {
			memCacheSize = 1;
		}
		mMemoryCache = new LruCache<String, Bitmap>(memCacheSize) {
			/**
			 * Measure item size in bytes rather than units which is more practical for a bitmap cache
			 */
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return getBitmapSize(bitmap);
			}

			/**
			 * 缓存满时，移除对象对应操作
			 */
			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

				if (mImageMemCacheEvent == null) {// 使用默认动作

					if (oldValue != null && !oldValue.isRecycled()) {
						DevUtil.v("jackzhou", String.format("mImageMemCache recycle:%s", oldValue));

						oldValue.recycle();
						oldValue = null;
					}

				} else {// 外面用户自定义的动作
					mImageMemCacheEvent.entryRemoved(evicted, key, oldValue, newValue);
				}

			};
		};
	}

	/**
	 * Get the size in bytes of a bitmap.
	 * 
	 * @param bitmap
	 * @return size in bytes
	 */
	@TargetApi(12)
	private static int getBitmapSize(Bitmap bitmap) {
		if (DevUtil.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion 否则使用将不显示加载图片
	 * 
	 * Sets the memory cache size based on a percentage of the device memory class. Eg. setting percent to 0.2 would set the memory cache to one fifth of the device memory class.
	 * Throws {@link IllegalArgumentException} if percent is < 0.05 or > .8.
	 * 
	 * This value should be chosen carefully based on a number of factors Refer to the corresponding Android Training class for more discussion:
	 * http://developer.android.com/training/displaying-bitmaps/
	 * 
	 * @param context
	 *            Context to use to fetch memory class
	 * @param percent
	 *            Percent of memory class to use to size memory cache
	 */
	private void setMemCacheSizePercent(Context context, float percent) {
		if (percent < 0.05f || percent > 0.8f) {
			throw new IllegalArgumentException("setMemCacheSizePercent - percent must be " + "between 0.05 and 0.8 (inclusive)");
		}
		memCacheSize = Math.round(percent * getMemoryClass(context) * 1024 * 1024);
	}

	/**
	 * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion 否则使用将不显示加载图片
	 * 
	 * @param context
	 * @return
	 */
	@TargetApi(5)
	private static int getMemoryClass(Context context) {
		int ret = 0;
		if (DevUtil.hasAndroid2_0()) {
			ret = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		}
		return ret;
	}

	/**
	 * 该接口对外提供用于处理缓存回收时的自定义动作
	 * 
	 * @author jackzhou
	 * 
	 */
	public interface ImageMemCacheEvent {
		/**
		 * 
		 * @param evicted
		 *            true if the entry is being removed to make space, false if the removal was caused by a put or remove.
		 * @param key
		 * @param oldValue
		 * @param newValue
		 *            the new value for key, if it exists. If non-null, this removal was caused by a put. Otherwise it was caused by an eviction or a remove.
		 */
		public void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue);
	}
}
