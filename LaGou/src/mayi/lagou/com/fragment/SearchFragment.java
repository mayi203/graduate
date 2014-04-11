package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-2
 */
public class SearchFragment extends BaseFragment {

	private GridView mGridView;
	private ArrayAdapter<String> adapter;
	private String[] tags = { "Java", "PHP", "Android", "ios", "UI", "产品经理",
			"运营", "前端", "BD", "实习" };

	@Override
	public int contentView() {
		return R.layout.f_search;
	}

	@Override
	public void findViewsById() {
		mGridView = (GridView) findViewById(R.id.gridview);
	}

	@Override
	public void initValue() {
		adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_tag,
				R.id.tag, tags);
		mGridView.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JobFragment.getInsatance().pageNum = 1;
				JobFragment.getInsatance().refreshData(tags[position],
						JobFragment.city, 1, "down");
				JobFragment.getInsatance().search.setText(tags[position]);
				JobFragment.getInsatance().search.setTextSize(18);
				JobFragment.getInsatance().search.setTextColor(Color.BLACK);
				getActivity().onBackPressed();
			}
		});
		findTextView(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
	}
}
