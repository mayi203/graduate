package mayi.lagou.com.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseActivity;

public class SplashActivity extends BaseActivity {

	private ImageView cloud_s, cloud_m, lagou;

	@Override
	public int contentView() {
		return R.layout.a_splash;
	}

	@Override
	public void findViewsById() {
		Animation sAnim = AnimationUtils.loadAnimation(this,
				R.anim.cloud_s_anim);
		Animation mAnim = AnimationUtils.loadAnimation(this,
				R.anim.cloud_m_anim);
		Animation lAnim = AnimationUtils.loadAnimation(this, R.anim.lagou_anim);
		cloud_s = findImageView(R.id.cloud_s);
		cloud_s.setAnimation(sAnim);
		cloud_m = findImageView(R.id.cloud_m);
		cloud_m.setAnimation(mAnim);
		lagou = findImageView(R.id.lagou);
		lagou.setAnimation(lAnim);
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
