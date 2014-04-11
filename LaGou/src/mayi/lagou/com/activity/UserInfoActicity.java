package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.LoginFragment;
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.fragment.UserInfoFragment;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class UserInfoActicity extends BaseFragmentActivity implements Refresh {

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
}
