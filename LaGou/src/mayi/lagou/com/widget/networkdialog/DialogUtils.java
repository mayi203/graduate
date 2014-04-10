package mayi.lagou.com.widget.networkdialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Looper;
import android.view.KeyEvent;

/**
 * 类说明
 * 
 * @author c00james
 * @version createDate：2013-3-15 下午3:45:01 定义Dialog，PopupWindow
 */
public class DialogUtils {
	public static Dialog updatedialog = null;
	public static NetProgressDialog processdialog;
	public static Timer t;

	public static NetProgressDialog showProcessDialog(final Activity activity) {
		return showProcessDialog(activity, false);
	}

	public static NetProgressDialog showProcessDialog(final Activity activity,
			boolean cancelable) {
		if (processdialog != null) {
			hideProcessDialog();
		}
		processdialog = NetProgressDialog.createDialog(activity);
		processdialog.setCancelable(cancelable);
		processdialog.show();
		t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if (processdialog != null && processdialog.isShowing()) {
					Looper.prepare();
					hideProcessDialog();
					Looper.loop();
				}
			}
		}, 25 * 1000 * 2);
		if (!cancelable)
			return processdialog;
		processdialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					processdialog.dismiss();
				}
				return true;
			}
		});
		return processdialog;
	}

	public static void hideProcessDialog() {
		if (processdialog != null) {
			processdialog.dismiss();
			processdialog = null;
			if (t != null) {
				t.cancel();
				t = null;
			}
		}
	}

}
