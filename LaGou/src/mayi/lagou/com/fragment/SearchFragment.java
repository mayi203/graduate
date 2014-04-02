package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 */

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-2
 */
public class SearchFragment extends BaseFragment {

	private String[] tags = { "Java", "PHP", "Android", "ios", "UI", "产品经理",
			"运营", "前端", "BD", "实习" };
	private LinearLayout mLinearLayout;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#contentView()
	 */
	@Override
	public int contentView() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mayi.lagou.com.core.BaseFragment#onCreateView(android.view.LayoutInflater
	 * , android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		return mLinearLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#findViewsById()
	 */
	@Override
	public void findViewsById() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#initValue()
	 */
	@Override
	public void initValue() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#initListener()
	 */
	@Override
	public void initListener() {

	}

	public void init() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(8, 8, 8, 8);
		mLinearLayout = new LinearLayout(getActivity());
		mLinearLayout.setLayoutParams(lp);
		for (int i = 0, len = tags.length; i < len; i++) {
			TextView item = new TextView(getActivity());
			item.setText(tags[i]);
			item.setTextSize(18);
			item.setLayoutParams(lp);
			item.setBackgroundResource(R.drawable.shap_search_item);
			mLinearLayout.addView(item);
		}
	}
}
