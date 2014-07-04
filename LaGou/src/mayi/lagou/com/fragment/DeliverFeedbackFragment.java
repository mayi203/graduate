/**
 * 
 */
package mayi.lagou.com.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.adapter.DeliverAdapter;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.DeliverFeedback;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-13
 */
public class DeliverFeedbackFragment extends BaseFragment {

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private DeliverAdapter mAdapter;
	private int pageNum=1;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	@Override
	public int contentView() {
		return R.layout.f_deliver_feedback;
	}

	@Override
	public void findViewsById() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.delive_list);
	}

	@Override
	public void initValue() {
		mPullToRefreshListView.setPullLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		mListView.setFadingEdgeLength(0);
		mListView.setDividerHeight(10);
		mListView.setSelector(android.R.color.transparent);
		mListView.setDivider(getResources().getDrawable(R.drawable.list_de));
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
		mPullToRefreshListView
		.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageNum = 1;
				loadData(pageNum,"down");
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageNum++;
				loadData(pageNum,"up");
			}
		});
	}
	private void setLastUpdateTime() {
		String text = formatDateTime(System.currentTimeMillis());
		mPullToRefreshListView.setLastUpdatedLabel(text);
	}

	private String formatDateTime(long time) {
		if (0 == time) {
			return "";
		}
		return mDateFormat.format(new Date(time));
	}

	@Override
	public void onResume() {
		super.onResume();
		getUserInfo();
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		String emailTxt = SharePreferenceUtil.getString(getActivity(), "email");
		map.put("email", emailTxt);
		String pswTxt = SharePreferenceUtil.getString(getActivity(), "psw");
		map.put("password", pswTxt);
		map.put("autoLogin", "1");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.LogIn, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String response) {
						if (statusCode == 200) {
							loadData(1,"up");
						}
					}
				});
	}

	private void loadData(int pageNum,final String type) {
		client.get(LaGouApi.Host + LaGouApi.DeliverRecord+pageNum,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, String content) {
						List<DeliverFeedback> list = ParserUtil.parseDeliverFeedback(content);
						if ("down".equals(type)){
							mAdapter.deleteAllItems();
							mAdapter.addItems(list);
							setLastUpdateTime();
							mPullToRefreshListView.onPullDownRefreshComplete();
						}else if ("up".equals(type)){
							mAdapter.addItems(list);
							mPullToRefreshListView.onPullUpRefreshComplete();
						}
					}
				});
	}
}
