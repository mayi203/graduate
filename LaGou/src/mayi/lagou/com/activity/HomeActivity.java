package mayi.lagou.com.activity;

import com.umeng.analytics.MobclickAgent;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.fragment.JobFragment;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;

/**
 * @author 203mayi@gmail.com 2014-5-6
 */
public class HomeActivity extends BaseFragmentActivity implements OnChangeUrl {

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
		mUrl = url;
	}

	@Override
	public String getUrl() {
		return mUrl;
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

}
