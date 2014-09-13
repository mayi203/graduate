package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.ExprienceAdapter;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyResumeFragment extends BaseFragment {

	private OnRequestInfo userInfo;
	private UserInfo mUserInfo;
	private ImageView userIcon;
	private ExprienceAdapter adapter;
	private ListView exList;
	private TextView baseInfo, expect, project, education, self, producation;
	String url;

	@Override
	public int contentView() {
		// getActivity().getActionBar().setTitle(R.string.resume);
		return R.layout.f_resume;
	}

	@Override
	public void findViewsById() {
		userIcon = findImageView(R.id.icon);
		baseInfo = findTextView(R.id.base);
		expect = findTextView(R.id.expect);
		project = findTextView(R.id.project);
		education = findTextView(R.id.education);
		self = findTextView(R.id.self);
		producation = findTextView(R.id.producation);
		exList = (ListView) findViewById(R.id.ex_list);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void initListener() {
		findTextView(R.id.back_resume).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						getActivity().onBackPressed();
					}
				});

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		userInfo = (UserInfoActicity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		init();
	}

	private void init() {
		mUserInfo = userInfo.getUserInfo();
		if (mUserInfo == null || "".equals(mUserInfo)) {
			return;
		}
		if (mUserInfo.getUserIcon() != null
				&& !"".equals(mUserInfo.getUserIcon())) {
			app().getImageLoader().loadImage(userIcon, mUserInfo.getUserIcon(),
					R.drawable.default_avatar);
		}
		baseInfo.setText(mUserInfo.getBasicInfo());
		expect.setText(mUserInfo.getJobExpect());
		adapter = new ExprienceAdapter(getActivity(), app().getImageLoader(),
				mUserInfo.getJobExperience());
		exList.setAdapter(adapter);
		StringBuilder pro = new StringBuilder();
		for (int i = 0, j = mUserInfo.getProjectExperience().size(); j > 0
				&& i < j; i++) {
			pro.append(mUserInfo.getProjectExperience().get(i).getProjectName()
					+ mUserInfo.getProjectExperience().get(i).getProjectTime()
					+ "\n"
					+ mUserInfo.getProjectExperience().get(i)
							.getProjectDetail());
		}
		project.setText(pro.toString());
		StringBuilder edu = new StringBuilder();
		for (int i = 0, j = mUserInfo.getEducationExperience().size(); j > 0
				&& i < j; i++) {
			edu.append(mUserInfo.getEducationExperience().get(i)
					.getEducationTime()
					+ "\n"
					+ mUserInfo.getEducationExperience().get(i).getSchool()
					+ mUserInfo.getEducationExperience().get(i).getMajor());
		}
		education.setText(edu.toString());
		StringBuilder produ = new StringBuilder();
		for (int i = 0, j = mUserInfo.getProjectShow().size(); j > 0 && i < j; i++) {
			produ.append(mUserInfo.getProjectShow().get(i).getProjectUrl()
					+ "\n"
					+ mUserInfo.getProjectShow().get(i).getProjectDetail()
					+ "\n");
		}
		producation.setText(produ.toString());
		self.setText(mUserInfo.getSelfDescription());
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (item.getItemId() == android.R.id.home) {
	// getActivity().getActionBar().setTitle(R.string.self);
	// getActivity().onBackPressed();
	// }
	// return super.onOptionsItemSelected(item);
	// }

}
