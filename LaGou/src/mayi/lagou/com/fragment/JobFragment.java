/**
 * 
 */
package mayi.lagou.com.fragment;

import java.util.List;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.adapter.JobItemAdapt;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.LaGouPosition;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import mayi.lagou.com.widget.pulltorefresh.PullToRefreshListView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class JobFragment extends BaseFragment implements OnClickListener {
	public static final String ARG_PLANET_NUMBER = "planet_number";
	private static final String TAG = "JobFragment";

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private JobItemAdapt adapter;
	private Button mMenuButton;
	private Button mItemButton1;
	private Button mItemButton2;
	private Button mItemButton3;
	private Button mItemButton4;
	private Button mItemButton5;
	private String cityName;
	private boolean mIsMenuOpen = false;
	private int radius;

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#contentView()
	 */
	@Override
	public int contentView() {
		return R.layout.f_job;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#findViewsById()
	 */
	@Override
	public void findViewsById() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.job_list);
		mMenuButton = findButton(R.id.menu);
		mItemButton1 = findButton(R.id.item1);
		mItemButton2 = findButton(R.id.item2);
		mItemButton3 = findButton(R.id.item3);
		mItemButton4 = findButton(R.id.item4);
		mItemButton5 = findButton(R.id.item5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#initValue()
	 */
	@Override
	public void initValue() {
		radius=app().getScreenWidth(getActivity())/2-30;
		mPullToRefreshListView.setPullLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		adapter = new JobItemAdapt(getActivity());
		mListView.setAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mayi.lagou.com.core.BaseFragment#initListener()
	 */
	@Override
	public void initListener() {
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {

					}
				});
		findViewById(R.id.lay_search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeFragment(new SearchFragment(), R.id.content_frame);
			}
		});
		mMenuButton.setOnClickListener(this);
		mItemButton1.setOnClickListener(this);
		mItemButton2.setOnClickListener(this);
		mItemButton3.setOnClickListener(this);
		mItemButton4.setOnClickListener(this);
		mItemButton5.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	private void refreshData() {
		client.get(LaGouApi.Host, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<LaGouPosition> list = ParserUtil.parserPosition(response);
				adapter.addItems(list);
				System.out.println(list.get(0).getCompany());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
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
		}
		cityName = findButton(v.getId()).getText().toString();
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

}
