package mayi.lagou.com.fragment;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.adapter.JobItemAdapt;
import mayi.lagou.com.data.Position;
import mayi.lagou.com.utils.ConfigCache;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public final class JobListFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";

	public static JobListFragment newInstance(String content) {
		JobListFragment fragment = new JobListFragment();
		fragment.jobType = content;

		return fragment;
	}

	private String jobType = "所有职位";
	private String city = "全国";
	public boolean isVisible;
	private boolean initComplate = false;
	private boolean isFirstLoad = true;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private JobItemAdapt adapter;
	private List<Position> allData;
	private int pageNum = 1;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private MyHandler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("lagou onCreate" + jobType);
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			jobType = savedInstanceState.getString(KEY_CONTENT);
		}
		mHandler=new MyHandler(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, jobType);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("lagou onCreateView" + jobType);
		View main = inflater.inflate(R.layout.f_job_list, null);
		mPullToRefreshListView = (PullToRefreshListView) main
				.findViewById(R.id.job_list);
		init();
		initListener();
		return main;
	}

	private void init() {
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

	private void initListener() {
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageNum = 1;
						refreshData(jobType, city, pageNum, "down", false);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pageNum++;
						refreshData(jobType, city, pageNum, "up", true);
					}
				});
		initComplate = true;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		isVisible = isVisibleToUser;
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			System.out.println("lagou setUserVisibleHint" + jobType);
			mHandler.sendEmptyMessageDelayed(REFRESHDATA, 500);
		} else {
			// 相当于Fragment的onPause
			DialogUtils.hideProcessDialog();
		}
	}

	private void refresh(){
		refreshData(jobType, city, pageNum, "down", true);
	}
	private static final int REFRESHDATA=1;
	class MyHandler extends Handler{

		private WeakReference<JobListFragment> fragment;
		public MyHandler(JobListFragment f){
			fragment=new WeakReference<JobListFragment>(f);
		}
		@Override
		public void handleMessage(Message msg) {
			JobListFragment listFragment=fragment.get();
			switch(msg.what){
			case REFRESHDATA:
				listFragment.refresh();
				break;
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	public void changeCity(String city) {
		this.city = city;
		refreshData(jobType, city, pageNum, "down", true);
	}

	/**
	 * type down清除现有，重新加载 up加载更多
	 */
	public void refreshData(String jType, String city, int pageNum,
			final String type, boolean useCache) {
		final String url = LaGouApi.Host + LaGouApi.Jobs + jType + "?city="
				+ city + "&pn=" + pageNum;
		String responseStr = ConfigCache.getUrlCache(url, getActivity());
		if (useCache && responseStr != null && !"".equals(responseStr)) {
			List<Position> list = ParserUtil.parserPosition(responseStr);
			if ("down".equals(type) && initComplate) {
				allData.clear();
				adapter.deleteAllItems();
				adapter.addItems(list);
				setLastUpdateTime();
				mPullToRefreshListView.onPullDownRefreshComplete();
			} else if ("up".equals(type) && initComplate) {
				adapter.addItems(list);
				mPullToRefreshListView.onPullUpRefreshComplete();
			}
			if (list != null && list.size() > 0 && initComplate) {
				allData.addAll(list);
			}
			isFirstLoad = false;
			return;
		}
		if (isFirstLoad) {
			DialogUtils.showProcessDialog(getActivity(), true);
		}
		LaGouApp.getInstance().client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Position> list = ParserUtil.parserPosition(response);
				if ("down".equals(type) && initComplate) {
					allData.clear();
					adapter.deleteAllItems();
					adapter.addItems(list);
					setLastUpdateTime();
					mPullToRefreshListView.onPullDownRefreshComplete();
				} else if ("up".equals(type) && initComplate) {
					adapter.addItems(list);
					mPullToRefreshListView.onPullUpRefreshComplete();
				}
				if (list != null && list.size() > 0 && initComplate) {
					allData.addAll(list);
				}
				isFirstLoad = false;
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
}
