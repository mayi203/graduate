package mayi.lagou.com.imageloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import mayi.lagou.com.imageloader.ImageMemCache.ImageMemCacheEvent;
import mayi.lagou.com.utils.DevUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * 
 * 图片加载类。<br>
 * 可单独在每个组件中自行使用，或在app全局使用该类的一个对象。（自行根据需要控制）<br>
 * <br>
 * 建议新应用可以将其包装为单例全局使用（一次性根据设备划分memory缓存大小）避免OOM。 <br>
 * 里面自带memory缓存及磁盘缓存。memory缓存大小设置应与所写app有关，请根据业务自行调整<br>
 * 
 */

public class ImageLoader {

	private Context mContext;

	/** 默认图片的一个缓存列表，图片通过resourceId只生成一次 */
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, Bitmap> mCacheOfloadingBitmap = new HashMap<Integer, Bitmap>();

	private ImageMemCache mImageMemCache;

	/** 设置图片显示 是否有FadeIn效果 **/
	private boolean mFadeInBitmap = true;

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
	@TargetApi(5)
	public ImageLoader(Context context, float percent) {
		this.mContext = context;
		this.mImageMemCache = new ImageMemCache(context, percent);
		init();
	}

	/**
	 * 设置缓存固定值大小
	 * 
	 * @param size
	 *            单位：字节
	 */
	public ImageLoader(Context context, int maxSize) {
		this.mContext = context;
		this.mImageMemCache = new ImageMemCache(maxSize);
		init();
	}

	private void init() {
		DevUtil.initialize(mContext);
	}

	private static final Executor singleThreadExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	});

	/**
	 * 异步加载图片<br>
	 * <br>
	 * 注意：<br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmap
	 *            加载图片时loading图
	 */
	public void loadImage(final ImageView imageView, String filePathOrUrl, final Bitmap loadingBitmap) {
		loadImage(imageView, filePathOrUrl, loadingBitmap, 0, 0, null, false);
	}

	/**
	 * 异步加载图片，可以按比例缩放及裁剪图片<br>
	 * <br>
	 * 重要：本方法中isNeedCut=true使用了sdk level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常 <br>
	 * <br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmap
	 *            加载图片时loading图
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param cachePath
	 *            缓存目录
	 * @param isNeedCut
	 *            缩放时是否需要裁剪 true:缩放后将大于指定width和height的裁剪掉 false:仅按比例缩放保证图片宽和高不大于width和height
	 */
	@TargetApi(8)
	public void loadImage(final ImageView imageView, String filePathOrUrl, final Bitmap loadingBitmap, int width, int height, String cachePath, boolean isNeedCut) {
		if (filePathOrUrl == null) {
			return;
		}

		/*
		 * 加载图片逻辑 1.mem缓存命中，直接显示 2.mem缓存未命中，磁盘缓存命中，使用独立线程池，与网络下载的线程池分开，提高加载速度 3.全未命中，第一次加载图片使用自定义的继承LibAsyncTask类，单线程队列下载和加载图片
		 */
		// memcache状态
		DevUtil.v("jackzhou", String.format("mImageMemCache status: size=%s - maxSize=%s - size*100/maxSize=%s - hitCount=%s - missCount=%s", mImageMemCache.size(),
				mImageMemCache.maxSize(), mImageMemCache.size() * 100 / mImageMemCache.maxSize(), mImageMemCache.hitCount(), mImageMemCache.missCount()));
		// mem缓存命中
		String memKey = generateMemCacheKey(filePathOrUrl, width, height, cachePath, isNeedCut);
		if (isMemCached(memKey)) {
			imageView.setImageDrawable(new LibBitmapDrawable(mContext.getResources(), mImageMemCache.get(memKey)));
			return;
		}

		// 磁盘缓存命中
		if (isDiskCached(filePathOrUrl, width, height, cachePath)) {

			if (BitmapWorkerTask.cancelPotentialWork(filePathOrUrl, imageView)) {

				final BitmapWorkerTask task = new BitmapWorkerTask(filePathOrUrl, imageView, loadingBitmap, mImageMemCache, width, height, cachePath, isNeedCut);
				task.setIsFadeInBitmap(mFadeInBitmap);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getResources(), loadingBitmap, task);
				imageView.setImageDrawable(asyncDrawable);

				task.executeOnExecutor(singleThreadExecutor);
				DevUtil.v("jackzhou", String.format("mImageMemCache ++++++++++++ Executor:singleThreadExecutor task:%s", task));
			}

			return;
		}

		// 第一次加载
		if (BitmapWorkerTask.cancelPotentialWork(filePathOrUrl, imageView)) {

			final BitmapWorkerTask task = new BitmapWorkerTask(filePathOrUrl, imageView, loadingBitmap, mImageMemCache, width, height, cachePath, isNeedCut);
			task.setIsFadeInBitmap(mFadeInBitmap);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getResources(), loadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);

			task.executeOnExecutor(LibAsyncTask.SERIAL_EXECUTOR);// 这里一次只会有一个tast执行
																	// BitmapWorkerTask继承新的AsyncTask。
			DevUtil.v("jackzhou", String.format("mImageMemCache ------------ Executor:LibAsyncTask.SERIAL_EXECUTOR task:%s", task));
		}

	}

	/**
	 * 异步加载图片<br>
	 * <br>
	 * 注意：<br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmapResId
	 *            加载图片时loading图资源id
	 */
	public void loadImage(final ImageView imageView, String filePathOrUrl, int loadingBitmapResId) {
		loadImage(imageView, filePathOrUrl, loadingBitmapResId, 0, 0, null, false);
	}

	/**
	 * 异步加载图片，可以按比例缩放图片 缩放保证图片宽和高不大于width和height<br>
	 * <br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmapResId
	 *            加载图片时loading图资源id
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	public void loadImage(final ImageView imageView, String filePathOrUrl, int loadingBitmapResId, int width, int height) {
		loadImage(imageView, filePathOrUrl, loadingBitmapResId, width, height, null, false);
	}

	/**
	 * 异步加载图片，可以按比例缩放及裁剪图片<br>
	 * <br>
	 * 重要：本方法中isNeedCut=true使用了sdk level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常 <br>
	 * <br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmapResId
	 *            加载图片时loading图
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param isNeedCut
	 *            缩放时是否需要裁剪 true:缩放后将大于指定width和height的裁剪掉 false:仅按比例缩放保证图片宽和高不大于width和height
	 */
	@TargetApi(8)
	public void loadImage(final ImageView imageView, String filePathOrUrl, int loadingBitmapResId, int width, int height, boolean isNeedCut) {
		loadImage(imageView, filePathOrUrl, loadingBitmapResId, width, height, null, isNeedCut);
	}

	/**
	 * 异步加载图片，可以按比例缩放及裁剪图片<br>
	 * <br>
	 * 重要：本方法中isNeedCut=true使用了sdk level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常 <br>
	 * <br>
	 * 该加载内置磁盘及内存缓存机制。<br>
	 * 图片在内存缓存满时，为防止OOM会自动recycle掉（已做处理，recycle后图片会透明，不会报：Trying to use recycled bitmap。为了app业务或美观建议设置ImageView的android:background属性。）<br>
	 * 
	 * @param imageView
	 *            显示图片的控件
	 * @param filePathOrUrl
	 *            加载图片的本地路径或网络url
	 * @param loadingBitmapResId
	 *            加载图片时loading图
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param cachePath
	 *            缓存目录
	 * @param isNeedCut
	 *            缩放时是否需要裁剪 true:缩放后将大于指定width和height的裁剪掉 false:仅按比例缩放保证图片宽和高不大于width和height
	 */
	@TargetApi(8)
	public void loadImage(ImageView imageView, String filePathOrUrl, int loadingBitmapResId, int width, int height, String cachePath, boolean isNeedCut) {

		// 加载时显示的默认图
		Bitmap loadingBitmap = mCacheOfloadingBitmap.get(loadingBitmapResId);
		if (loadingBitmap == null) {
			loadingBitmap = BitmapFactory.decodeResource(imageView.getResources(), loadingBitmapResId);
			mCacheOfloadingBitmap.put(loadingBitmapResId, loadingBitmap);
		}

		loadImage(imageView, filePathOrUrl, loadingBitmap, width, height, cachePath, isNeedCut);
	}

	/**
	 * 清空Memory缓存，所有图片会被recycle. 已作处理，可放心使用，recycle的图片不会报：trying to use a recycled bitmap错误<br>
	 * 建议在onPause及onDestroy中调用该方法
	 * 
	 */
	public void clearMemoryCache() {
		mImageMemCache.clearCache();
	}

	/**
	 * 移除单个Memory缓存，图片会被recycle. 已作处理，可放心使用，recycle的图片不会报：trying to use a recycled bitmap错误<br>
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap removeMemoryCache(String key) {
		return mImageMemCache.remove(key);
	}

	/**
	 * 得到Memory缓存内容
	 * 
	 * @return
	 */
	public Map<String, Bitmap> snapshot() {
		return mImageMemCache.snapshot();
	}

	/**
	 * 设置memory缓存大小
	 * 
	 * @param maxSize
	 */
	public void setMemCacheSize(int maxSize) {
		mImageMemCache.setCacheSize(maxSize);
	}

	/**
	 * 是否在内存中缓存
	 * 
	 * @return
	 */
	public boolean isMemCached(String memKey) {

		Bitmap temp = mImageMemCache.get(memKey);

		if (temp == null) {
			return false;
		} else {
			DevUtil.v("jackzhou", String.format("mImageMemCache memcache hit. memKey = '%s'", memKey));
			return true;
		}
	}

	/**
	 * 是否磁盘缓存
	 * 
	 * @param filePathOrUrl
	 * @param width
	 * @param height
	 * @param cachePath
	 * @return
	 */
	private boolean isDiskCached(String filePathOrUrl, int width, int height, String cachePath) {

		return ImageHelper.getInstance(mContext).isDiskCached(filePathOrUrl, width, height, cachePath);
	}

	/**
	 * 生成memory缓存的key
	 * 
	 * @param filePathOrUrl
	 * @param width
	 * @param height
	 * @param cachePath
	 * @param isNeedCut
	 * @return
	 */
	public static String generateMemCacheKey(String filePathOrUrl, int width, int height, String cachePath, boolean isNeedCut) {
		return String.format("%s%s%s%s%s", String.valueOf(filePathOrUrl), width, height, String.valueOf(cachePath), isNeedCut);
	}

	/**
	 * 对外提供用于处理缓存回收时的自定义动作。不设置则默认：将图片recycle
	 * 
	 * @param imageMemCacheEvent
	 */
	public void setImageMemCacheEvent(ImageMemCacheEvent imageMemCacheEvent) {
		mImageMemCache.setImageMemCacheEvent(imageMemCacheEvent);
	}

	/**
	 * 设置图片显示 是否有FadeIn效果
	 * 
	 * @param isFadeInBitmap
	 */
	public void setIsFadeInBitmap(boolean isFadeInBitmap) {
		mFadeInBitmap = isFadeInBitmap;
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mImageMemCache.put(key, bitmap);
		}
	}

	/**
	 * 保存bitmap到sd卡
	 * 
	 * @param bitmap
	 *            待保存的bitmap
	 * @param string
	 *            待保存的目录路径
	 * @param fileName
	 *            待保存的文件名
	 * @return 图片路径或者null
	 * 
	 */
	public String saveImage2Local(Bitmap bitmap, String string, String fileName) {
		return saveImage2Local(bitmap, string, fileName, Bitmap.CompressFormat.JPEG, 100);
	}

	/**
	 * 保存bitmap到sd卡
	 * 
	 * @param bitmap
	 *            待保存的bitmap
	 * @param path
	 *            待保存的目录路径
	 * @param fileName
	 *            待保存的文件名
	 * @param compress
	 *            图片压缩率
	 * @return 图片路径或者null
	 */
	public String saveImage2Local(Bitmap bitmap, String path, String fileName, CompressFormat format, int compress) {
		File imagePath = null;
		try {
			if (bitmap != null && !bitmap.isRecycled()) {

				File imgDir = new File(path);
				if (!imgDir.exists()) {// 如果存储的不存在，先创建
					imgDir.mkdirs();
				}

				imagePath = new File(path, fileName);// 给新照的照片文件命名

				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));

				/* 采用压缩转档方法 */
				bitmap.compress(format, compress, bos);

				/* 调用flush()方法，更新BufferStream */
				bos.flush();

				/* 结束OutputStream */
				bos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (imagePath != null && bitmap != null) {
			return imagePath.toString();
		} else {
			return null;
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mImageMemCache.get(key);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

}
