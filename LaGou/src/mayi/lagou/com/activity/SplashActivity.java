package mayi.lagou.com.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
		cloud_s = findImageView(R.id.cloud_s);
		cloud_m = findImageView(R.id.cloud_m);
        //摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0f, 100f, 50, 50);
        alphaAnimation2.setDuration(800);
        alphaAnimation2.setRepeatCount(Animation.INFINITE);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
        cloud_s.setAnimation(alphaAnimation2);
        cloud_m.setAnimation(alphaAnimation2);
        alphaAnimation2.start();
        Animation lAnim=AnimationUtils.loadAnimation(this, R.anim.lagou_anim);
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
