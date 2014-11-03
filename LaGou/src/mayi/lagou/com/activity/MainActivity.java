package mayi.lagou.com.activity;

import java.util.ArrayList;
import java.util.List;
import mayi.lagou.com.R;
import mayi.lagou.com.fragment.JobListFragment;
import mayi.lagou.com.view.TabPageIndicator;
import mayi.lagou.com.view.arcmenu.ArcMenu;
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

public class MainActivity extends FragmentActivity {
	private static String[] JobList;
	private static String[] CityList;
	private List<JobListFragment> list = new ArrayList<JobListFragment>();
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera,
			R.drawable.composer_music, R.drawable.composer_place,
			R.drawable.composer_sleep, R.drawable.composer_thought,
			R.drawable.composer_with };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		JobList = getResources().getStringArray(R.array.job_list);
		CityList=getResources().getStringArray(R.array.city_list);
		FragmentPagerAdapter adapter = new GoogleMusicAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
		initArcMenu(arcMenu, ITEM_DRAWABLES);
	}

	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).isVisible() && list.get(i).isVisible) {
							list.get(i).changeCity(CityList[position]);
						}
					}
					Toast.makeText(MainActivity.this, CityList[position],
							Toast.LENGTH_SHORT).show();
				}
			});
		}
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
}
