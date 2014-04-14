package mayi.lagou.com.fragment;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.adapter.ExprienceAdapter;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;

public class MyResumeFragment extends BaseFragment {

	private OnRequestInfo userInfo;
	private UserInfo mUserInfo;
	private ImageView userIcon;
	private ExprienceAdapter adapter;
	private ListView exList;
	private TextView baseInfo, expect, project, education, self,
			producation;
	String url;

	@Override
	public int contentView() {
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
		exList=(ListView)findViewById(R.id.ex_list);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void initListener() {

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
		app().getImageLoader().loadImage(userIcon, mUserInfo.getUserIcon(),
				R.drawable.default_avatar);
		baseInfo.setText(mUserInfo.getBasicInfo());
		expect.setText(mUserInfo.getJobExpect()+"\n");
		StringBuilder exp = new StringBuilder();
		for (int i = 0, j = mUserInfo.getJobExperience().size(); j > 0 && i < j; i++) {
			exp.append(mUserInfo.getJobExperience().get(i).getCompanyName());
		}
		adapter=new ExprienceAdapter(getActivity(), app().getImageLoader(),mUserInfo.getJobExperience());
		exList.setAdapter(adapter);
		StringBuilder pro = new StringBuilder();
		for (int i = 0, j = mUserInfo.getProjectExperience().size(); j > 0
				&& i < j; i++) {
			pro.append(mUserInfo.getProjectExperience().get(i).getProjectName()
					+ mUserInfo.getProjectExperience().get(i).getProjectTime()+"\n"
					+ mUserInfo.getProjectExperience().get(i)
							.getProjectDetail() + "\n");
		}
		project.setText(pro.toString());
		StringBuilder edu = new StringBuilder();
		for (int i = 0, j = mUserInfo.getEducationExperience().size(); j > 0
				&& i < j; i++) {
			edu.append(mUserInfo.getEducationExperience().get(i)
					.getEducationTime()
					+ mUserInfo.getEducationExperience().get(i).getSchool()
					+ mUserInfo.getEducationExperience().get(i).getMajor()
					+ "\n");
		}
		education.setText(edu.toString());
		StringBuilder produ = new StringBuilder();
		for (int i = 0, j = mUserInfo.getProjectShow().size(); j > 0 && i < j; i++) {
			produ.append(mUserInfo.getProjectShow().get(i).getProjectUrl()+"\n"
					+ mUserInfo.getProjectShow().get(i).getProjectDetail()
					+ "\n");
		}
		producation.setText(produ.toString());
		self.setText(mUserInfo.getSelfDescription()+"\n");
	}

}
