package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.LoginFragment;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class UserInfoActicity extends BaseFragmentActivity {

	@Override
	public int contentView() {
		return R.layout.a_user_info;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {

	}

	public void refresh() {
		String email = SharePreferenceUtil.getString(this, "userInfo");
		if (email == null || "".equals(email)) {
			changeFragment(new LoginFragment(), R.id.u_contain);
		} else {
		}
	}
}
