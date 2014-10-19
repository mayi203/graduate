package mayi.lagou.com.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.adapter.JobItemAdapt;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.Position;
import mayi.lagou.com.utils.ConfigCache;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;

public class JobListFragment extends BaseFragment{
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private JobItemAdapt adapter;
	public String mCity = "全国";
	public String jobType = "所有职位";
	public int pageNum = 1;
	List<Position> allData;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	@Override
	public int contentView() {
		return R.layout.f_job_list;
	}

	@Override
	public void findViewsById() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.job_list);
	}

	@Override
	public void initValue() {
		mPullToRefreshListView.setPullLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		mListView.setFadingEdgeLength(0);
		mListView.setDividerHeight(10);
		mListView.setSelector(android.R.color.transparent);
		mListView.setDivider(getResources().getDrawable(R.drawable.list_de));
		adapter = new JobItemAdapt(getActivity());
		mListView.setAdapter(adapter);
		allData = new ArrayList<Position>();
	}

	@Override
	public void initListener() {
		mPullToRefreshListView
		.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageNum = 1;
				refreshData(jobType, mCity, pageNum, "down");
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageNum++;
				refreshData(jobType, mCity, pageNum, "up");
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onChangeUrl.setUrl(allData.get(position).getPositionUrl());
				addFragmentToStack(R.id.contain, new JobDetailFragment());
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		Bundle args = getArguments();
		jobType = args != null ? args.getString("text") : "所有职位";
		if (NetWorkState.isNetWorkConnected(getActivity())) {
			refreshData(jobType, mCity, 1, "down");
		} else {
			loadCacheData(jobType, mCity, 1, "down");
			afresh();
			findViewById(R.id.lay_search).setClickable(false);
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mPullToRefreshListView.setVisibility(View.VISIBLE);
				findViewById(R.id.lay_search).setClickable(true);
				refreshData(jobType, mCity, 1, "down");
			}
			super.handleMessage(msg);
		}
	};
	private void afresh() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!NetWorkState.isNetWorkConnected(getActivity())) {
					try {
						Thread.sleep(1000);
						if (NetWorkState.isNetWorkConnected(getActivity())) {
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
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
	private boolean loadCacheData(String jType, String city, int pageNum,
			final String type){
		if ("".equals(city))
			return true;
		city = mCity;
		jobType = jType;
		final String url = LaGouApi.Host + LaGouApi.Jobs + jType + "?city="
				+ city + "&pn=" + pageNum;
		String responseStr = ConfigCache.getUrlCache(url, getActivity());
		if (responseStr != null && !"".equals(responseStr)) {
			List<Position> list = ParserUtil.parserPosition(responseStr);
			if ("down".equals(type)) {
				allData.clear();
				adapter.deleteAllItems();
				adapter.addItems(list);
				setLastUpdateTime();
				mPullToRefreshListView.onPullDownRefreshComplete();
			} else if ("up".equals(type)) {
				adapter.addItems(list);
				mPullToRefreshListView.onPullUpRefreshComplete();
			}
			if (list != null && list.size() > 0) {
				allData.addAll(list);
			}
			return true;
		}else{
			return false;
		}
	}
	/**
	 * type down清除现有，重新加载 up加载更多
	 */
	public void refreshData(String jType, String city, int pageNum,
			final String type) {
		if(loadCacheData(jType,city,pageNum,
				type)){
			return;
		}
		final String url = LaGouApi.Host + LaGouApi.Jobs + jType + "?city="
				+ city + "&pn=" + pageNum;
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Position> list = ParserUtil.parserPosition(response);
				if ("down".equals(type)) {
					allData.clear();
					adapter.deleteAllItems();
					adapter.addItems(list);
					setLastUpdateTime();
					mPullToRefreshListView.onPullDownRefreshComplete();
				} else if ("up".equals(type)) {
					adapter.addItems(list);
					mPullToRefreshListView.onPullUpRefreshComplete();
				}
				if (list != null && list.size() > 0) {
					allData.addAll(list);
				}
				ConfigCache.setUrlCache(response, url);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT)
						.show();
				super.onFailure(arg0, arg1, arg2, arg3);
			}

			@Override
			public void onFinish() {
				DialogUtils.hideProcessDialog();
				super.onFinish();
			}
		});
	}
}
