package mayi.lagou.com.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

/**
 * 该类用于解决图片被recycle后报：Trying to use recycled bitmap的问题
 * 
 */
public class LibBitmapDrawable extends BitmapDrawable {

	public LibBitmapDrawable(Resources resources, Bitmap bitmap) {
		super(resources, bitmap);
	}

	@Override
	public void draw(Canvas canvas) {

		// 判断bitmap是否被recycle
		Bitmap temp = getBitmap();
		if (temp == null || temp.isRecycled()) {
			canvas.drawColor(Color.TRANSPARENT);
			return;
		}

		super.draw(canvas);
	}
}
