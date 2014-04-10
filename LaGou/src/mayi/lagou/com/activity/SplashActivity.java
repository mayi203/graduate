package mayi.lagou.com.activity;

import android.content.Intent;
import android.os.Handler;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseActivity;

public class SplashActivity extends BaseActivity {

	@Override
	public int contentView() {
		return R.layout.a_splash;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
	}

	@Override
	public void initListener() {

	}

}
