package mayi.lagou.com.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.HomeActivity;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.JobItemAdapt;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.Position;
import mayi.lagou.com.utils.ACache;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.view.circularmenu.FloatingActionButton;
import mayi.lagou.com.view.circularmenu.FloatingActionMenu;
import mayi.lagou.com.view.circularmenu.SubActionButton;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class JobFragment extends BaseFragment implements OnClickListener,
		OnMenuItemClickListener, AnimationListener {

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private OnChangeUrl onChangeUrl;
	private boolean isFirstLoad = true;
	private JobItemAdapt adapter;
	public int pageNum = 1;
	public String mCity = "全国";
	public String jobType = "所有职位";
	private String[] jobArray;
	private String[] cityArray;
	private RelativeLayout hideLay, screenLay;
	private GridView gridView;
	private EditText hideEdit;
	private List<Position> allData;
	private static JobFragment instance;
	private ImageView rightBar;
	private ACache mCache;
	private TextView screen;
	private FloatingActionMenu rightLowerMenu;
	private FloatingActionButton rightLowerButton;
	private ImageView rlIcon1, rlIcon2, rlIcon3, rlIcon4, rlIcon5, rlIcon6;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	@Override
	public int contentView() {
		return R.layout.f_job;
	}

	@Override
	public void findViewsById() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.job_list);
		rightBar = findImageView(R.id.right_bar_job);
		hideLay = (RelativeLayout) findViewById(R.id.hide_lay);
		screenLay = (RelativeLayout) findViewById(R.id.screen_lay);
		gridView = (GridView) findViewById(R.id.ser_items);
		hideEdit = (EditText) findViewById(R.id.ser_txt);
		screen = (TextView) findTextView(R.id.screen);
	}

	@Override
	public void initValue() {
		instance = this;
		intiCircalMenu();
		mCache = ACache.get(getActivity());
		jobArray = getResources().getStringArray(R.array.job_list);
		cityArray = getResources().getStringArray(R.array.city_list);
		mPullToRefreshListView.setPullLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		mListView.setFadingEdgeLength(0);
		mListView.setDividerHeight(10);
		mListView.setSelector(android.R.color.transparent);
		mListView.setDivider(getResources().getDrawable(R.drawable.list_de));
		adapter = new JobItemAdapt(getActivity());
		mListView.setAdapter(adapter);
		allData = new ArrayList<Position>();
		refreshData(jobType, mCity, 1, "down");
		if (NetWorkState.isNetWorkConnected(getActivity())) {
		} else {
			afresh();
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
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
		rightBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (LaGouApp.isLogin) {
					showPopMenu(rightBar, R.menu.job_home2);
				} else {
					showPopMenu(rightBar, R.menu.jb_home);
				}
			}
		});
		hideLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (showJobSer) {
					hideJobSer();
				} else if (showCitySer) {
					hideCitySer();
				}
				hideSoftInput();
			}
		});
		screenLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showJobSer(jobArray);
			}
		});
		hideEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (showCitySer) {
						mCity = hideEdit.getText().toString().trim();
						hideCitySer();
					} else if (showJobSer) {
						jobType = hideEdit.getText().toString().trim();
						hideJobSer();
					}
					screen.setText(jobType + "." + mCity);
					refreshData(jobType, mCity, 1, "down");
				}
				return false;
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

		public void showMenu();
	}

	public static JobFragment getInsatance() {
		return instance;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mPullToRefreshListView.setVisibility(View.VISIBLE);
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
		String responseStr = mCache.getAsString(url);
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
				mCache.put(url, response, 2 * ACache.TIME_HOUR);
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
		selectCity(v.getId());
	}

	private void selectCity(int i) {
		rightLowerMenu.close(true);
		if (i == 106) {
			showCitySer(cityArray);
		} else {

		}
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

	private void intiCircalMenu() {
		ImageView fabIconNew = new ImageView(getActivity());
		fabIconNew.setImageDrawable(getResources().getDrawable(
				R.drawable.location));
		rightLowerButton = new FloatingActionButton.Builder(getActivity())
				.setContentView(fabIconNew).build();

		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(
				getActivity());
		rlIcon1 = new ImageView(getActivity());
		rlIcon2 = new ImageView(getActivity());
		rlIcon3 = new ImageView(getActivity());
		rlIcon4 = new ImageView(getActivity());
		rlIcon5 = new ImageView(getActivity());
		rlIcon6 = new ImageView(getActivity());
		rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.guang));
		rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.shen));
		rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.hang));
		rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.hu));
		rlIcon5.setImageDrawable(getResources().getDrawable(R.drawable.jing));
		rlIcon6.setImageDrawable(getResources().getDrawable(R.drawable.quan));

		rightLowerMenu = new FloatingActionMenu.Builder(getActivity())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon6).build())
				.attachTo(rightLowerButton).build();
		rlIcon1.setId(101);
		rlIcon1.setOnClickListener(this);
		rlIcon2.setId(102);
		rlIcon2.setOnClickListener(this);
		rlIcon3.setId(103);
		rlIcon3.setOnClickListener(this);
		rlIcon4.setId(104);
		rlIcon4.setOnClickListener(this);
		rlIcon5.setId(105);
		rlIcon5.setOnClickListener(this);
		rlIcon6.setId(106);
		rlIcon6.setOnClickListener(this);
	}

	private Animation jobInAnimation, jobOutAnimation, cityInAnimation,
			cityOutAnimation;

	private boolean showJobSer = false, showCitySer = false;

	private void showJobSer(final String[] array) {
		if (jobInAnimation == null) {
			jobInAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.ser_job_in);
		}
		jobInAnimation.setFillAfter(true);
		hideLay.setVisibility(View.VISIBLE);
		gridView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.search_item, array));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hideEdit.setText(array[position]);
			}
		});
		hideLay.startAnimation(jobInAnimation);
		showJobSer = true;
		showSoftInput();
	}

	private void showSoftInput() {
		hideEdit.setFocusable(true);
		hideEdit.setFocusableInTouchMode(true);
		hideEdit.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) hideEdit
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(hideEdit, 0);
	}

	private void hideJobSer() {
		if (jobOutAnimation == null) {
			jobOutAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.ser_job_out);
		}
		jobOutAnimation.setAnimationListener(this);
		jobOutAnimation.setFillAfter(true);
		hideLay.startAnimation(jobOutAnimation);
	}

	private void showCitySer(final String[] array) {
		if (cityInAnimation == null) {
			cityInAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.ser_city_in);
		}
		cityInAnimation.setFillAfter(true);
		hideLay.setVisibility(View.VISIBLE);
		gridView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.search_item, array));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hideEdit.setText(array[position]);
			}
		});
		hideLay.startAnimation(cityInAnimation);
		showCitySer = true;
		showSoftInput();
	}

	private void hideCitySer() {
		if (cityOutAnimation == null) {
			cityOutAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.ser_city_out);
		}
		cityOutAnimation.setAnimationListener(this);
		cityOutAnimation.setFillAfter(true);
		hideLay.startAnimation(cityOutAnimation);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == jobOutAnimation || animation == cityOutAnimation) {
			gridView.setAdapter(null);
			hideLay.clearAnimation();
			gridView.setOnItemClickListener(null);
			hideLay.setVisibility(View.GONE);
			showJobSer = false;
			showCitySer = false;
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
