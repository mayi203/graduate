package mayi.lagou.com.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.DeliverAdapter;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.DeliverFeedback;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;
import mayi.lagou.com.utils.ConfigCache;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author 203mayi@gmail.com 2014-5-6
 */
public class UserInfoFragment extends BaseFragment implements OnClickListener {

	private TextView userInfo, fbNull;
	private ImageView userHead;
	private View headView;
	private OnRequestInfo onRequest;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private OnChangeUrl onChangeUrl;
	List<DeliverFeedback> allData;
	private DeliverAdapter mAdapter;
	private int pageNum = 1;
	private boolean isFirstLoad = true;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	public UserInfoFragment() {

	}

	@Override
	public int contentView() {
		// getActivity().getActionBar().setTitle(R.string.self);
		return R.layout.f_user_info;
	}

	@Override
	public void findViewsById() {
		userInfo = findTextView(R.id.base_info);
		userHead = findImageView(R.id.user_icon);
		headView = findViewById(R.id.lay_info);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.delive_list);
		fbNull = findTextView(R.id.fb_null);
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
		allData = new ArrayList<DeliverFeedback>();
		String content = SharePreferenceUtil.getString(getActivity(),
				"userInfo");
		if (content != null && !"".equals(content)) {
			initData(content);
			isFirstLoad = false;
		}
		if (NetWorkState.isNetWorkConnected(getActivity())) {
			getUserInfo();
		} else {
			loadFeedBackData(1, "up");
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void initListener() {
		headView.setOnClickListener(this);
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageNum = 1;
						loadFeedBackData(pageNum, "down");
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageNum++;
						loadFeedBackData(pageNum, "up");
					}
				});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onChangeUrl.setUrl(allData.get(position).getPositionUrl());
				addFragmentToStack(R.id.u_contain, new JobDetailFragment());
			}
		});
		findTextView(R.id.back_info).setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onRequest = (UserInfoActicity) activity;
		onChangeUrl = (UserInfoActicity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
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

	private void getUserInfo() {
		if (isFirstLoad) {
			DialogUtils.showProcessDialog(getActivity(), true);
		}
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
							try {
								JSONObject object = new JSONObject(response);
								String success = object.optString("success");
								if ("true".equals(success) && !isExit) {
									SharePreferenceUtil.putString(
											getActivity(), "userId", object
													.optJSONObject("content")
													.optString("userid"));
									getResume(LaGouApi.Host + LaGouApi.Resume);
									loadFeedBackData(1, "up");
								} else if ("false".equals(success) && !isExit) {
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void getResume(String url) {
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				initData(content);
				if (!isExit) {
					SharePreferenceUtil.putString(getActivity(), "userInfo",
							content);
				}
				isFirstLoad = false;
				Log.v("lagou", content);
			}

			@Override
			public void onFinish() {
				DialogUtils.hideProcessDialog();
				super.onFinish();
			}
		});
	}

	private void initData(String content) {
		UserInfo user = ParserUtil.parserUserInfo(content);
		onRequest.setUserInfo(user);
		userInfo.setText(user.getBasicInfo());
		if (user.getUserIcon() != null && !"".equals(user.getUserIcon())) {
			app().getImageLoader().loadImage(userHead, user.getUserIcon(),
					R.drawable.default_avatar);
		}
	}

	private void loadFeedBackData(final int pageNum, final String type) {
		final String url = LaGouApi.Host + LaGouApi.DeliverRecord + pageNum;
		String contentStr = ConfigCache.getUrlCache(url, getActivity());
		if (contentStr != null && !"".equals(contentStr)) {
			List<DeliverFeedback> list = ParserUtil
					.parseDeliverFeedback(contentStr);
			if (pageNum == 1 && list.size() == 0) {
				fbNull.setVisibility(View.VISIBLE);
			} else {
				fbNull.setVisibility(View.GONE);
			}
			if ("down".equals(type)) {
				allData.clear();
				mAdapter.deleteAllItems();
				mAdapter.addItems(list);
				setLastUpdateTime();
				mPullToRefreshListView.onPullDownRefreshComplete();
			} else if ("up".equals(type)) {
				mAdapter.addItems(list);
				mPullToRefreshListView.onPullUpRefreshComplete();
			}
			if (list != null && list.size() > 0) {
				allData.addAll(list);
			}
			isFirstLoad = false;
			return;
		}
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, String content) {
				List<DeliverFeedback> list = ParserUtil
						.parseDeliverFeedback(content);
				if (pageNum == 1 && list.size() == 0) {
					fbNull.setVisibility(View.VISIBLE);
				} else {
					fbNull.setVisibility(View.GONE);
				}
				if ("down".equals(type)) {
					allData.clear();
					mAdapter.deleteAllItems();
					mAdapter.addItems(list);
					setLastUpdateTime();
					mPullToRefreshListView.onPullDownRefreshComplete();
				} else if ("up".equals(type)) {
					mAdapter.addItems(list);
					mPullToRefreshListView.onPullUpRefreshComplete();
				}
				if (list != null && list.size() > 0) {
					allData.addAll(list);
				}
				isFirstLoad = false;
				ConfigCache.setUrlCache(content, url);
			}

			@Override
			public void onFinish() {
				DialogUtils.hideProcessDialog();
				super.onFinish();
			}
		});
	}

	public interface OnRequestInfo {
		public void setUserInfo(UserInfo user);

		public UserInfo getUserInfo();

	}

	private boolean isExit = false;

	@Override
	public void onPause() {
		isExit = true;
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_info:
			addFragmentToStack(R.id.u_contain, new MyResumeFragment());
			break;
		case R.id.back_info:
			getActivity().onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroyView() {
		DialogUtils.hideProcessDialog();
		super.onDestroyView();
	}
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (item.getItemId() == android.R.id.home) {
	// getActivity().onBackPressed();
	// }
	// return super.onOptionsItemSelected(item);
	// }
}
