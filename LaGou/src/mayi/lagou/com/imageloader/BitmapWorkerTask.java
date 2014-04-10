package mayi.lagou.com.imageloader;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

/**
 * 由http://developer.android.com/training/displaying-bitmaps/index.html<br>
 * 中BitmapFun.zip demo更改
 * 
 * The actual AsyncTask that will asynchronously process the image.
 */
public class BitmapWorkerTask extends LibAsyncTask<Void, Void, Bitmap> {

	private Context mContext;
	private String mFilePathOrUrl;
	private Bitmap mLoadingBitmap;
	private final WeakReference<ImageView> imageViewReference;

	private boolean mFadeInBitmap = true;
	protected Resources mResources;
	private static final int FADE_IN_TIME = 300;

	private ImageMemCache mImageMemCache;
	private int mWidth;
	private int mHeight;
	private String mCachePath;
	private boolean mIsNeedCut;

	public BitmapWorkerTask(String filePathOrUrl, ImageView imageView, Bitmap loadingBitmap, ImageMemCache imageMemCache, int width, int height, String cachePath, boolean isNeedCut) {
		this.mFilePathOrUrl = filePathOrUrl;
		this.mLoadingBitmap = loadingBitmap;
		mContext = imageView.getContext();
		mResources = mContext.getResources();
		imageViewReference = new WeakReference<ImageView>(imageView);
		mImageMemCache = imageMemCache;
		mWidth = width;
		mHeight = height;
		mCachePath = cachePath;
		mIsNeedCut = isNeedCut;
	}

	/**
	 * Background processing.
	 */
	final static int Nomal = 0;
	final static int InSample = 1;
	final static int InSampleAndCut = 2;

	@Override
	protected Bitmap doInBackground(Void... params) {

		if (isCancelled()) {
			return null;
		}

		String memKey = ImageLoader.generateMemCacheKey(mFilePathOrUrl, mWidth, mHeight, mCachePath, mIsNeedCut);
		Bitmap temp = mImageMemCache.get(memKey);// 优化 相同图片mFilePathOrUrl
													// 缓存命中则直接返回
		if (temp == null) {
			temp = ImageHelper.getInstance(mContext).loadImage(mFilePathOrUrl, mWidth, mHeight, mCachePath, mIsNeedCut);
		}

		return temp;
	}

	/**
	 * Once the image is processed, associates it to the imageView
	 */
	@Override
	protected void onPostExecute(Bitmap bitmap) {

		if (isCancelled()) {
			bitmap = null;
		}

		final ImageView imageView = getAttachedImageView();

		if (bitmap != null && imageView != null) {
			String memKey = ImageLoader.generateMemCacheKey(mFilePathOrUrl, mWidth, mHeight, mCachePath, mIsNeedCut);
			mImageMemCache.put(memKey, bitmap);
			setImageBitmap(imageView, bitmap);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onCancelled(Bitmap result) {

	}

	/**
	 * Returns the ImageView associated with this task as long as the ImageView's task still points to this task as well. Returns null otherwise.
	 */
	private ImageView getAttachedImageView() {
		final ImageView imageView = imageViewReference.get();
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (this == bitmapWorkerTask) {
			return imageView;
		}

		return null;
	}

	/**
	 * Called when the processing is complete and the final bitmap should be set on the ImageView.
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
		if (mFadeInBitmap) {
			// Transition drawable with a transparent drwabale and the final
			// bitmap
			final TransitionDrawable td = new TransitionDrawable(new Drawable[] { new ColorDrawable(android.R.color.transparent), new LibBitmapDrawable(mResources, bitmap) });

			// Set background to loading bitmap
			// imageView.setBackgroundDrawable(new BitmapDrawable(mResources,
			// mLoadingBitmap));
			// 更改效果，使用setImageDrawable
			imageView.setImageDrawable(new BitmapDrawable(mResources, mLoadingBitmap));

			imageView.setImageDrawable(td);
			td.startTransition(FADE_IN_TIME);
		} else {
			imageView.setImageDrawable(new LibBitmapDrawable(mContext.getResources(), bitmap));
		}
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with this imageView. null if there is no such task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * Returns true if the current work has been canceled or if there was no work in progress on this image view. Returns false if the work in progress deals with the same data.
	 * The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(String mFilePathOrUrl, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final String bitmapUrl = bitmapWorkerTask.mFilePathOrUrl;
			if (bitmapUrl == null || !bitmapUrl.equals(mFilePathOrUrl)) {
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置图片显示 是否有FadeIn效果
	 * 
	 * @param isFadeInBitmap
	 */
	public void setIsFadeInBitmap(boolean isFadeInBitmap) {
		mFadeInBitmap = isFadeInBitmap;
	}
}