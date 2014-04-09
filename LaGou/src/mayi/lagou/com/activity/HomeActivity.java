package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.JobFragment;

public class HomeActivity extends BaseFragmentActivity{

	@Override
	public int contentView() {
		return R.layout.a_home;
	}

	@Override
	public void findViewsById() {
		
	}

	@Override
	public void initValue() {
		addFragmentToContainer(new JobFragment(), R.id.contain);
	}

	@Override
	public void initListener() {
		
	}

}
