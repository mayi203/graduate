package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.PositionDetail;
import mayi.lagou.com.utils.ParserUtil;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class JobDetailFragment extends BaseFragment {

	private TextView desTxt;
	private String mUrl;

	public JobDetailFragment() {

	}

	public JobDetailFragment(String url) {
		mUrl = url;
	}

	@Override
	public int contentView() {
		return R.layout.f_job_detail;
	}

	@Override
	public void findViewsById() {
		desTxt = findTextView(R.id.des);
	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {

	}

	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	private void refreshData() {
		client.get(mUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				PositionDetail detail = ParserUtil
						.parserPositionDetail(response);
				desTxt.setText(detail.getPositionName());
			}
		});
	}
}
