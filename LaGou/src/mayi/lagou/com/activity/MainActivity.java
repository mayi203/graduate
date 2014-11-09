package mayi.lagou.com.activity;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.fragment.JobListFragment;
import mayi.lagou.com.fragment.SettingFragment;
import mayi.lagou.com.fragment.JobListFragment.OnChangeUrl;
import mayi.lagou.com.view.TabPageIndicator;
import mayi.lagou.com.view.circularmenu.FloatingActionButton;
import mayi.lagou.com.view.circularmenu.FloatingActionMenu;
import mayi.lagou.com.view.circularmenu.SubActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnChangeUrl ,OnMenuItemClickListener{
	private static String[] JobList;
	private static String[] CityList;
	private List<JobListFragment> list = new ArrayList<JobListFragment>();
	private FloatingActionMenu rightLowerMenu;
	private FloatingActionButton rightLowerButton;
	private JobListFragment currentFragment=null;
	private ImageView rightTab,rlIcon1, rlIcon2, rlIcon3, rlIcon4,rlIcon5,rlIcon6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		JobList = getResources().getStringArray(R.array.job_list);
		CityList = getResources().getStringArray(R.array.city_list);
		FragmentPagerAdapter adapter = new GoogleMusicAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		intiCircalMenu();
		
		initListener();
	}

	private void initListener(){
		rightTab=(ImageView)findViewById(R.id.right_bar_job);
		rightTab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (LaGouApp.isLogin) {
					showPopMenu(rightTab, R.menu.job_home2);
				} else {
					showPopMenu(rightTab, R.menu.jb_home);
				}				
			}
		});
	}
	private void intiCircalMenu() {
		ImageView fabIconNew = new ImageView(this);
		fabIconNew.setImageDrawable(getResources().getDrawable(
				R.drawable.location));
		rightLowerButton = new FloatingActionButton.Builder(
				this).setContentView(fabIconNew).build();

		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
		rlIcon1 = new ImageView(this);
		rlIcon2 = new ImageView(this);
		rlIcon3 = new ImageView(this);
		rlIcon4 = new ImageView(this);
		rlIcon5 = new ImageView(this);
		rlIcon6 = new ImageView(this);
		rlIcon1.setImageDrawable(getResources().getDrawable(
				R.drawable.guang));
		rlIcon2.setImageDrawable(getResources().getDrawable(
				R.drawable.shen));
		rlIcon3.setImageDrawable(getResources().getDrawable(
				R.drawable.hang));
		rlIcon4.setImageDrawable(getResources().getDrawable(
				R.drawable.hu));
		rlIcon5.setImageDrawable(getResources().getDrawable(
				R.drawable.jing));
		rlIcon6.setImageDrawable(getResources().getDrawable(
				R.drawable.quan));

		rightLowerMenu = new FloatingActionMenu.Builder(this)
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

	@Override
	public void onClick(View v) {
		selectCity(v.getId());
	}

	private void selectCity(int i) {
		rightLowerMenu.close(true);
		currentFragment.changeCity(CityList[(i - 101)]);
		Toast.makeText(this, CityList[i - 101], Toast.LENGTH_SHORT).show();
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			JobListFragment fragment = JobListFragment
					.newInstance(JobList[position % JobList.length]);
			list.add(fragment);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return JobList[position % JobList.length];
		}

		@Override
		public int getCount() {
			return JobList.length;
		}
	}

	private String mUrl;

	@Override
	public void setUrl(String url) {
		mUrl = url;
		rightLowerMenu.close(false);
		rightLowerButton.setVisibility(View.GONE);
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void showMenu() {
		rightLowerButton.setVisibility(View.VISIBLE);
	}
	private void showPopMenu(View v, int res) {
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener(this);
		popup.inflate(res);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getItemId() == R.id.login) {
			startActivity(new Intent(this,UserInfoActicity.class));
		} else if (item.getItemId() == R.id.setting) {
			rightLowerButton.setVisibility(View.GONE);
			currentFragment.addFragmentToStack(R.id.contain, new SettingFragment());
		}
		return false;
	}

	@Override
	public void setCurrentFragment(JobListFragment fragment) {
		this.currentFragment=fragment;
	}
}
