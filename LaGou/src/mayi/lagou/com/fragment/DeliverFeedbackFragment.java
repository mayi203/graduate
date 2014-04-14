/**
 * 
 */
package mayi.lagou.com.fragment;

import java.util.List;

import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.DeliverAdapter;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.DeliverFeedback;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;
import mayi.lagou.com.utils.ParserUtil;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-13
 */
public class DeliverFeedbackFragment extends BaseFragment {

	private ListView mListView;
	private DeliverAdapter mAdapter;
	private OnRequestInfo mInfo;

	@Override
	public int contentView() {
		return R.layout.f_deliver_feedback;
	}

	@Override
	public void findViewsById() {
		mListView = findListView(android.R.id.list);
	}

	@Override
	public void initValue() {
		mAdapter = new DeliverAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void initListener() {
		findTextView(R.id.deliver_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getActivity().onBackPressed();
					}
				});
	}

	@Override
	public void onAttach(Activity activity) {
		mInfo = (UserInfoActicity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		setList(mInfo.getDeliver());
	}

	private void setList(String deliver) {
		List<DeliverFeedback> list = ParserUtil.parseDeliverFeedback(deliver);
		mAdapter.addItems(list);
	}
}
