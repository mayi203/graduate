/**
 * 
 */
package mayi.lagou.com.core;

import java.lang.reflect.Field;

import mayi.lagou.com.LaGouApp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public abstract class BaseActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getOverflowMenu();
		setContentView(contentView());
		findViewsById();
		initValue();
		initListener();
	}

	public void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract int contentView();

	/** 获取布局中所有的view */
	public abstract void findViewsById();

	/** 初始化数据 */
	public abstract void initValue();

	/** 监听事件 */
	public abstract void initListener();

	protected LaGouApp app() {
		return (LaGouApp) getApplication();
	}

	public TextView findTextView(int id) {
		return (TextView) findViewById(id);
	}

	public EditText findEditText(int id) {
		return (EditText) findViewById(id);
	}

	public Button findButton(int id) {
		return (Button) findViewById(id);
	}

	public ImageView findImageView(int id) {
		return (ImageView) findViewById(id);
	}

	public ListView findListView(int id) {
		return (ListView) findViewById(id);
	}

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		intent.putExtra("fromActivity", getClass().getName());
		startActivity(intent);
	}

	public void startActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent(this, cls);
		intent.putExtra("fromActivity", getClass().getName());
		startActivityForResult(intent, requestCode);
	}

	public boolean isComeTheActivity(Class<?> cls) {
		if (!getIntent().hasExtra("fromActivity")) {
			return false;
		}
		return cls.getName().equals(getIntent().getStringExtra("fromActivity"));
	}

	public Intent intent(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		return intent;
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput() {
		try {
			((InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(this.getCurrentFocus()
							.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}
}