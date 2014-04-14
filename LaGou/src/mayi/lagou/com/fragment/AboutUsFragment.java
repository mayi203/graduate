/**
 * 
 */
package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-14
 */
public class AboutUsFragment extends BaseFragment {

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#contentView()
	 */
	@Override
	public int contentView() {
		return R.layout.f_about_us;
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
		findTextView(R.id.about_us_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getActivity().onBackPressed();
					}
				});
	}

}
