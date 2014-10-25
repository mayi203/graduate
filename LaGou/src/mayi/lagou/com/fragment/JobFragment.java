package mayi.lagou.com.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.HomeActivity;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.JobItemAdapt;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.Position;
import mayi.lagou.com.utils.AppCommonUtil;
import mayi.lagou.com.utils.ConfigCache;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class JobFragment extends BaseFragment implements OnClickListener,
		OnMenuItemClickListener {
	private static final String TAG = "JobFragment";

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	// public TextView search;
	private OnChangeUrl onChangeUrl;
	private boolean isFirstLoad = true;
	private JobItemAdapt adapter;
	private Button mMenuButton;
	private Button mItemButton1, mItemButton2, mItemButton3, mItemButton4,
			mItemButton5;
	private String cityName;
	private boolean mIsMenuOpen = false;
	private int radius;
	public int pageNum = 1;
	public String mCity = "全国";
	public String jobType = "所有职位";
	List<Position> allData;
	private static JobFragment instance;
	private String[] jobList;
	private TextView[] tvList;
	private LinearLayout laySearch;
	private View grayView;
	private ImageView rightTab;
	// private View girlLay;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	@Override
	public int contentView() {
		return R.layout.f_job;
	}

	@Override
	public void findViewsById() {
		// search = findTextView(R.id.seatch_txt);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.job_list);
		mMenuButton = findButton(R.id.menu);
		mItemButton1 = findButton(R.id.item1);
		mItemButton2 = findButton(R.id.item2);
		mItemButton3 = findButton(R.id.item3);
		mItemButton4 = findButton(R.id.item4);
		mItemButton5 = findButton(R.id.item5);
		rightTab = findImageView(R.id.right_bar_job);
		laySearch = (LinearLayout) findViewById(R.id.lay_search);
		grayView = findViewById(R.id.grayview);
		grayView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN && mIsMenuOpen) {
					mIsMenuOpen = false;
					doAnimateClose(mItemButton1, 0, 5, radius);
					doAnimateClose(mItemButton2, 1, 5, radius);
					doAnimateClose(mItemButton3, 2, 5, radius);
					doAnimateClose(mItemButton4, 3, 5, radius);
					doAnimateClose(mItemButton5, 4, 5, radius);
				}
				return false;
			}
		});
		// girlLay=findViewById(R.id.girl_lay);
	}

	@SuppressWarnings("static-access")
	@Override
	public void initValue() {
		instance = this;
		jobList = getActivity().getResources().getStringArray(R.array.job_list);
		tvList = new TextView[jobList.length];
		radius = app().getScreenWidth(getActivity()) * 2 / 5;
		mPullToRefreshListView.setPullLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		mListView.setFadingEdgeLength(0);
		mListView.setDividerHeight(10);
		mListView.setSelector(android.R.color.transparent);
		mListView.setDivider(getResources().getDrawable(R.drawable.list_de));
		adapter = new JobItemAdapt(getActivity());
		mListView.setAdapter(adapter);
		allData = new ArrayList<Position>();
		int tvMinWidth = AppCommonUtil.dip2px(getActivity(), 40);
		for (int i = 0, j = jobList.length; i < j; i++) {
			TextView tv = new TextView(getActivity());
			tv.setPadding(0, 0, 30, 0);
			tv.setMinWidth(tvMinWidth);
			tv.setTextSize(20);
			tv.setTextColor(Color.rgb(120, 120, 120));
			tv.setGravity(Gravity.CENTER);
			tv.setText(jobList[i]);
			tv.setId(100 + i);
			tv.setOnClickListener(new JobListClickListener());
			laySearch.addView(tv);
			tvList[i] = tv;
		}
		tvList[0].setTextColor(Color.rgb(1, 152, 117));
		lastClick = 100;
		refreshData(jobType, mCity, 1, "down");
		if (NetWorkState.isNetWorkConnected(getActivity())) {
		} else {
			afresh();
			mMenuButton.setClickable(false);
			findViewById(R.id.lay_search).setClickable(false);
			// mPullToRefreshListView.setVisibility(View.GONE);
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
		}
	}

	private int lastClick = 0;

	private class JobListClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			tvList[id - 100].setTextColor(Color.rgb(1, 152, 117));
			if (lastClick != 0) {
				tvList[lastClick - 100].setTextColor(Color.rgb(120, 120, 120));
			}
			lastClick = id;
			jobType = jobList[id - 100];
			isFirstLoad = true;
			refreshData(jobType, mCity, 1, "down");
		}
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
		findViewById(R.id.lay_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// addFragmentToStack(R.id.contain, new SearchFragment());
			}
		});
		mMenuButton.setOnClickListener(this);
		mItemButton1.setOnClickListener(this);
		mItemButton2.setOnClickListener(this);
		mItemButton3.setOnClickListener(this);
		mItemButton4.setOnClickListener(this);
		mItemButton5.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onChangeUrl.setUrl(allData.get(position).getPositionUrl());
				addFragmentToStack(R.id.contain, new JobDetailFragment());
			}
		});
		findTextView(R.id.back_job).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}
		});
		rightTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (LaGouApp.isLogin) {
					showPopMenu(rightTab, R.menu.job_home2);
				} else {
					showPopMenu(rightTab, R.menu.jb_home);
				}
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onChangeUrl = (HomeActivity) activity;
	}

	public interface OnChangeUrl {
		public void setUrl(String url);

		public String getUrl();
	}

	public static JobFragment getInsatance() {
		return instance;
	}

	@Override
	public void onResume() {
		super.onResume();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				getActivity().getWindow().invalidatePanelMenu(
						Window.FEATURE_OPTIONS_PANEL);
			}
		};
		new Timer().schedule(task, 500);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mPullToRefreshListView.setVisibility(View.VISIBLE);
				// mMenuButton.setClickable(true);
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

	/**
	 * type down清除现有，重新加载 up加载更多
	 */
	public void refreshData(String jType, String city, int pageNum,
			final String type) {
		if ("".equals(city))
			return;
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
			isFirstLoad = false;
			return;
		}
		if (isFirstLoad) {
			DialogUtils.showProcessDialog(getActivity(), true);
		}
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
				isFirstLoad = false;
				ConfigCache.setUrlCache(response, url);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// girlLay.setVisibility(View.VISIBLE);
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

	@Override
	public void onClick(View v) {
		cityName = findButton(v.getId()).getText().toString();
		findButton(v.getId()).setText(mCity);
		if (!mIsMenuOpen) {
			mIsMenuOpen = true;
			doAnimateOpen(mItemButton1, 0, 5, radius);
			doAnimateOpen(mItemButton2, 1, 5, radius);
			doAnimateOpen(mItemButton3, 2, 5, radius);
			doAnimateOpen(mItemButton4, 3, 5, radius);
			doAnimateOpen(mItemButton5, 4, 5, radius);
		} else {
			mIsMenuOpen = false;
			doAnimateClose(mItemButton1, 0, 5, radius);
			doAnimateClose(mItemButton2, 1, 5, radius);
			doAnimateClose(mItemButton3, 2, 5, radius);
			doAnimateClose(mItemButton4, 3, 5, radius);
			doAnimateClose(mItemButton5, 4, 5, radius);
			if (!cityName.equals(mCity)) {
				mCity = cityName;
				refreshData(jobType, cityName, pageNum, "down");
			}
		}
		mMenuButton.setText(cityName);
	}

	/**
	 * 打开菜单的动画
	 * 
	 * @param view
	 *            执行动画的view
	 * @param index
	 *            view在动画序列中的顺序
	 * @param total
	 *            动画序列的个数
	 * @param radius
	 *            动画半径
	 */
	private void doAnimateOpen(View view, int index, int total, int radius) {
		if (view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
		grayView.setVisibility(View.VISIBLE);
		double degree = Math.PI * index / ((total - 1) * 2) + 3 / 2 * Math.PI;
		int translationX = (int) (radius * Math.cos(degree));
		int translationY = (int) (radius * Math.sin(degree));
		Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d",
				degree, translationX, translationY));
		AnimatorSet set = new AnimatorSet();
		// 包含平移、缩放和透明度动画
		set.playTogether(
				ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
				ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
				ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
				ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
				ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
		// 动画周期为500ms
		set.setDuration(1 * 500).start();
	}

	/**
	 * 关闭菜单的动画
	 * 
	 * @param view
	 *            执行动画的view
	 * @param index
	 *            view在动画序列中的顺序
	 * @param total
	 *            动画序列的个数
	 * @param radius
	 *            动画半径
	 */
	private void doAnimateClose(final View view, int index, int total,
			int radius) {
		if (view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
		grayView.setVisibility(View.GONE);
		double degree = Math.PI * index / ((total - 1) * 2) + 3 / 2 * Math.PI;
		int translationX = (int) (radius * Math.cos(degree));
		int translationY = (int) (radius * Math.sin(degree));
		Log.d(TAG, String.format("degree=%f, translationX=%d, translationY=%d",
				degree, translationX, translationY));
		AnimatorSet set = new AnimatorSet();
		// 包含平移、缩放和透明度动画
		set.playTogether(
				ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
				ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
				ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
				ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
				ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));
		// 为动画加上事件监听，当动画结束的时候，我们把当前view隐藏
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				view.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}
		});

		set.setDuration(1 * 500).start();
	}

	@Override
	public void onDestroyView() {
		DialogUtils.hideProcessDialog();
		super.onDestroyView();
	}

	private void showPopMenu(View v, int res) {
		PopupMenu popup = new PopupMenu(getActivity(), v);
		popup.setOnMenuItemClickListener(this);
		popup.inflate(res);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getItemId() == R.id.login) {
			startActivity(UserInfoActicity.class);
		} else if (item.getItemId() == R.id.setting) {
			addFragmentToStack(R.id.contain, new SettingFragment());
		}
		return false;
	}
	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// if (LaGouApp.isLogin) {
	// inflater.inflate(R.menu.job_home2, menu);
	// } else {
	// inflater.inflate(R.menu.jb_home, menu);
	// }
	// super.onCreateOptionsMenu(menu, inflater);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (item.getItemId() == R.id.login) {
	// MobclickAgent.onEvent(getActivity(), "self");
	// startActivity(UserInfoActicity.class);
	// } else if (item.getItemId() == android.R.id.home) {
	// MobclickAgent.onEvent(getActivity(), "self");
	// getActivity().onBackPressed();
	// } else if (item.getItemId() == R.id.setting) {
	// addFragmentToStack(R.id.contain, new SettingFragment());
	// }
	// return super.onOptionsItemSelected(item);
	// }

}
