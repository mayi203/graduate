package mayi.lagou.com.activity;

import java.util.ArrayList;

import mayi.lagou.com.R;
import mayi.lagou.com.adapter.NewsFragmentPagerAdapter;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.JobListFragment;
import mayi.lagou.com.utils.AppCommonUtil;
import mayi.lagou.com.view.ColumnHorizontalScrollView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseFragmentActivity{
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** 新闻分类列表*/
	private String[] newsClassify;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	@Override
	public int contentView() {
		return R.layout.a_main;
	}

	@Override
	public void findViewsById() {
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
	}

	@Override
	public void initValue() {
		mScreenWidth = AppCommonUtil.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		setChangelView();
	}

	@Override
	public void initListener() {
		
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		try{
			newsClassify=getResources().getStringArray(R.array.job_list);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** 
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  newsClassify.length;
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, new LinearLayout(this), rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 10;
			params.rightMargin = 10;
			TextView localTextView = new TextView(this);
			localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
			localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			localTextView.setGravity(Gravity.CENTER);
			localTextView.setPadding(5, 0, 5, 0);
			localTextView.setId(i);
			localTextView.setText(newsClassify[i]);
			localTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				localTextView.setSelected(true);
			}
			localTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  mViewPager.setCurrentItem(i);
				          }
			          }
			          Toast.makeText(getApplicationContext(), newsClassify[v.getId()], Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(localTextView, i ,params);
		}
	}
	/** 
	 *  初始化Fragment
	 * */
	private void initFragment() {
		int count =  newsClassify.length;
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", newsClassify[i]);
			JobListFragment newfragment = new JobListFragment();
			newfragment.setArguments(data);
			fragments.add(newfragment);
		}
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	/** 
	 *  ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};
}
