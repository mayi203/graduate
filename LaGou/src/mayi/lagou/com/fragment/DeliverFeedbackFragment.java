/**
 * 
 */
package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.widget.ListView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-13
 */
public class DeliverFeedbackFragment extends BaseFragment {

	private ListView mListView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#contentView()
	 */
	@Override
	public int contentView() {
		return R.layout.f_deliver_feedback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#findViewsById()
	 */
	@Override
	public void findViewsById() {
		mListView = findListView(android.R.id.list);
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
		// TODO Auto-generated method stub

	}

}
