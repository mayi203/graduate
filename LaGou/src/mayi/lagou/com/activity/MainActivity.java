package mayi.lagou.com.activity;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.R;
import mayi.lagou.com.fragment.JobListFragment;
import mayi.lagou.com.fragment.JobListFragment.OnChangeUrl;
import mayi.lagou.com.view.TabPageIndicator;
import mayi.lagou.com.view.circularmenu.FloatingActionButton;
import mayi.lagou.com.view.circularmenu.FloatingActionMenu;
import mayi.lagou.com.view.circularmenu.SubActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnChangeUrl {
	private static String[] JobList;
	private static String[] CityList;
	private List<JobListFragment> list = new ArrayList<JobListFragment>();
	private FloatingActionMenu rightLowerMenu;
	private FloatingActionButton rightLowerButton;
	private ImageView rlIcon1, rlIcon2, rlIcon3, rlIcon4;

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
	}

	private void intiCircalMenu() {
		ImageView fabIconNew = new ImageView(this);
		fabIconNew.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_new_light));
		rightLowerButton = new FloatingActionButton.Builder(
				this).setContentView(fabIconNew).build();

		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
		rlIcon1 = new ImageView(this);
		rlIcon2 = new ImageView(this);
		rlIcon3 = new ImageView(this);
		rlIcon4 = new ImageView(this);
		rlIcon1.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_chat_light));
		rlIcon2.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_camera_light));
		rlIcon3.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_video_light));
		rlIcon4.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_place_light));

		rightLowerMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
				.attachTo(rightLowerButton).build();
		rlIcon1.setId(101);
		rlIcon1.setOnClickListener(this);
		rlIcon2.setId(102);
		rlIcon2.setOnClickListener(this);
		rlIcon3.setId(103);
		rlIcon3.setOnClickListener(this);
		rlIcon4.setId(104);
		rlIcon4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		selectCity(v.getId());
	}

	private void selectCity(int i) {
		rightLowerMenu.close(true);
		for (int k = 0; k < list.size(); k++) {
			if (list.get(k) instanceof JobListFragment && list.get(k).isVisible) {
				list.get(k).changeCity(CityList[i - 100]);
			}
		}
		Toast.makeText(this, CityList[i - 100], Toast.LENGTH_SHORT).show();
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
}
