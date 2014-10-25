/**
 * 
 */
package mayi.lagou.com.core;

import com.loopj.android.http.AsyncHttpClient;

import mayi.lagou.com.LaGouApp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public abstract class BaseFragment extends Fragment {
	public AsyncHttpClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setHasOptionsMenu(true);
		// getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		client = new AsyncHttpClient();
		findViewsById();
		initValue();
		initListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(contentView(), container, false);
	}

	public abstract int contentView();

	/** 获取布局中所有的view */
	public abstract void findViewsById();

	/** 初始化数据 */
	public abstract void initValue();

	/** 监听事件 */
	public abstract void initListener();

	protected LaGouApp app() {
		return (LaGouApp) getActivity().getApplication();
	}

	@Override
	public void onDestroyView() {
		client.cancelRequests(getActivity(), true);
		super.onDestroyView();
	}

	public TextView findTextView(int id) {
		return (TextView) getActivity().findViewById(id);
	}

	public Button findButton(int id) {
		return (Button) getActivity().findViewById(id);
	}

	public ImageView findImageView(int id) {
		return (ImageView) getActivity().findViewById(id);
	}

	public ListView findListView(int id) {
		return (ListView) getActivity().findViewById(id);
	}

	public View findViewById(int id) {
		return getActivity().findViewById(id);
	}

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(getActivity(), cls);
		intent.putExtra("fromActivity", getActivity().getClass().getName());
		getActivity().startActivity(intent);
	}

	public void startActivity(Intent intent) {
		intent.putExtra("fromActivity", getActivity().getClass().getName());
		getActivity().startActivity(intent);
	}

	public void startActivityForResult(Class<?> cls, int requestCode) {
		Intent intent = new Intent(getActivity(), cls);
		intent.putExtra("fromActivity", getActivity().getClass().getName());
		getActivity().startActivityForResult(intent, requestCode);
	}

	public void addFragmentToStack(int container, Fragment fragment) {
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);// 设置动画效果
		ft.addToBackStack(null);
		ft.hide(this);
		ft.add(container, fragment);
		ft.commit();
	}

	public void addFragmentToContainer(Fragment fragment, int container) {
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.add(container, fragment);
		ft.commit();
	}

	public void showFragmentToContainer(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.show(fragment);
		ft.commit();
	}

	public void changeFragment(Fragment fragment, int container) {
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		ft.replace(container, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// 设置动画效果
		ft.commit();
	}

	public void backToLastFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		try {
			getActivity().getSupportFragmentManager().popBackStack();
		} catch (Exception e) {
		}
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput() {
		try {
			((InputMethodManager) getActivity().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getActivity().getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
		}
	}
}
