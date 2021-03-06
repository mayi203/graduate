package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;
import mayi.lagou.com.fragment.LoginFragment;
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.fragment.UserInfoFragment;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;
import mayi.lagou.com.utils.SharePreferenceUtil;

import com.umeng.analytics.MobclickAgent;

public class UserInfoActicity extends BaseFragmentActivity implements Refresh,
		OnRequestInfo, OnChangeUrl {

	private UserInfo mInfo;
	private String mUrl;

	@Override
	public int contentView() {
		return R.layout.a_user_info;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {
		refresh();
	}

	@Override
	public void initListener() {

	}

	@Override
	public void refresh() {
		String email = SharePreferenceUtil.getString(this, "email");
		if (email == null || "".equals(email)) {
			changeFragment(new LoginFragment(), R.id.u_contain);
		} else {
			changeFragment(new UserInfoFragment(), R.id.u_contain);
		}
	}

	@Override
	public void setUserInfo(UserInfo user) {
		mInfo = user;
	}

	@Override
	public UserInfo getUserInfo() {
		return mInfo;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

}
