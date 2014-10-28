package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.view.arcmenu.ArcMenu;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public final class JobListFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera,
		R.drawable.composer_music, R.drawable.composer_place,
		R.drawable.composer_sleep, R.drawable.composer_thought,
		R.drawable.composer_with };
	TextView text;

	public static JobListFragment newInstance(String content) {
		JobListFragment fragment = new JobListFragment();
		fragment.mContent = content;

		return fragment;
	}

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View main = inflater.inflate(R.layout.f_job_list, null);
		TextView text = (TextView) main.findViewById(R.id.txt);
		text.setText(mContent);
		ArcMenu arcMenu = (ArcMenu) main.findViewById(R.id.arc_menu);
		initArcMenu(arcMenu, ITEM_DRAWABLES);
		return main;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}
	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(getActivity());
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "position:" + position,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
