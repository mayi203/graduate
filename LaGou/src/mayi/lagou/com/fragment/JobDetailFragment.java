package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.PositionDetail;
import mayi.lagou.com.utils.ParserUtil;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class JobDetailFragment extends BaseFragment {

	private TextView title, require, release_time, com_name, com_del, details;
	private String mUrl;
	private ImageView com_img;

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
		title = findTextView(R.id.title);
		require = findTextView(R.id.require);
		release_time = findTextView(R.id.release_time);
		com_name = findTextView(R.id.com_name);
		com_del = findTextView(R.id.com_del);
		com_img = findImageView(R.id.com_img);
		details = findTextView(R.id.details);
	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {
		findTextView(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
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
				title.setText(detail.getPositionName());
				require.setText(detail.getSalary() + "/" + detail.getCity()
						+ "/" + detail.getExperience() + "/"
						+ detail.getEducation() + "/" + detail.getJobCategory()
						+ "\n" + detail.getJobTempt().trim());
				release_time.setText(detail.getReleaseTime());
				app().getImageLoader().loadImage(com_img,
						detail.getComIconUrl(), R.drawable.waiting);
				com_name.setText(detail.getCompany().trim());
				com_del.setText(detail.getField().trim() + "|"
						+ detail.getScale() + "|" + detail.getStage() + "\n"
						+ "地址：" + detail.getAddress());
				details.setText(detail.getJobDetail());
			}
		});
	}
}
