/**
 * 
 */
package mayi.lagou.com.core;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.LaGouApp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
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
public abstract class BaseFragmentActivity extends FragmentActivity {
	public FragmentTransaction ft;
	public List<Fragment> fragmentList = new ArrayList<Fragment>();
	public Fragment curFragment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(contentView());
		findViewsById();
		initValue();
		initListener();
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

	public void setActionBarTitle(String title) {
		setTitle(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
	}

	public void setActionBarTitle(int titleId) {
		setActionBarTitle(getResources().getString(titleId));
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

	public void addFragmentToStack(Fragment fragment, int container) {
		if (fragment == null) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);// 设置动画效果
		ft.addToBackStack(null);
		ft.add(container, fragment);
		ft.commit();
	}

	public void addFragmentToContainer(Fragment fragment, int container) {
		if (fragment == null) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(container, fragment);
		ft.commit();
	}

	public void showFragmentToContainer(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragment);
		ft.commit();
	}

	public void changeFragment(Fragment fragment, int container) {
		if (fragment == null) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// 设置动画效果
		ft.commitAllowingStateLoss();
	}

	public void backToLastFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		getSupportFragmentManager().popBackStack();
	}

	public void switchContent(Fragment from, Fragment to, int container,
			int type) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (!to.isAdded() && type == 1) { // 先判断是否被add�?
			transaction.hide(from).add(container, to).commit(); // 隐藏当前的fragment，add下一个到Activity�?
		} else {
			transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下�?��
		}
	}
}
