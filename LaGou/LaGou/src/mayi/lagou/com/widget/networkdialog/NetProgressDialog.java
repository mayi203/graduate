package mayi.lagou.com.widget.networkdialog;

import mayi.lagou.com.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

/**
 * 类说明
 * 
 * @author c00james
 * @version createDate：2013-4-10 上午11:02:32 网络请求缓冲Progress
 */
public class NetProgressDialog extends Dialog {
	private static NetProgressDialog customProgressDialog = null;

	public NetProgressDialog(Context context) {
		super(context);
	}

	public NetProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static NetProgressDialog createDialog(Context context) {
		customProgressDialog = new NetProgressDialog(context, R.style.Mydialog);
		customProgressDialog
				.setContentView(R.layout.dialog_popuptwindow_process);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.setCancelable(false);
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.dialog_process_image);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}
}