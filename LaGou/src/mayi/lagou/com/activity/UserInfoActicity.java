package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.LoginFragment;
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.fragment.UserInfoFragment;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class UserInfoActicity extends BaseFragmentActivity implements Refresh,
		OnRequestInfo {

	private UserInfo mInfo;
	private String mDeliver;

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
	public void setDeliver(String deliver) {
		mDeliver = deliver;
	}

	@Override
	public String getDeliver() {
		return mDeliver;
	}
}
