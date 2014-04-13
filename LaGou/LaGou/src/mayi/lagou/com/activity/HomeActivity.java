package mayi.lagou.com.activity;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.JobFragment;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;

public class HomeActivity extends BaseFragmentActivity implements OnChangeUrl{

	private String mUrl;
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

	@Override
	public void setUrl(String url) {
		mUrl=url;
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

}
