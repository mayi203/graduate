package mayi.lagou.com.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseActivity;

public class SplashActivity extends BaseActivity {

	private ImageView cloud_s, cloud_m, lagou,cloud_s2;

	@Override
	public int contentView() {
		return R.layout.a_splash;
	}

	@Override
	public void findViewsById() {
		Animation sAnim = AnimationUtils.loadAnimation(this,
				R.anim.cloud_s_anim_right);
		final Animation sAnim2=AnimationUtils.loadAnimation(this, R.anim.cloud_s_anim_left);
		Animation mAnim = AnimationUtils.loadAnimation(this,
				R.anim.cloud_m_anim);
		Animation lAnim = AnimationUtils.loadAnimation(this, R.anim.lagou_anim);
		cloud_s = findImageView(R.id.cloud_s);
		cloud_s.setAnimation(sAnim);
		sAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				sAnim2.cancel();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				cloud_s2=findImageView(R.id.cloud_s2);
				cloud_s2.setAnimation(sAnim2);
				cloud_s.setVisibility(View.GONE);
				cloud_s2.setVisibility(View.VISIBLE);
			}
		});
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
