package mayi.lagou.com.activity;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseActivity;

public class SplashActivity extends BaseActivity {

	private TextView lagou, lagouDel;

	@Override
	public int contentView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		return R.layout.a_splash;
	}

	@Override
	public void findViewsById() {
		lagou = findTextView(R.id.lagou);
		lagouDel = findTextView(R.id.lagou_del);
	}

	@Override
	public void initValue() {
		Animation lAnim = AnimationUtils.loadAnimation(this, R.anim.lagou_anim);
		final Animation dAnim = AnimationUtils.loadAnimation(this,
				R.anim.lagou_del_anim);
		lagou.setAnimation(lAnim);
		lAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				lagouDel.setVisibility(View.VISIBLE);
				lagouDel.setAnimation(dAnim);
			}
		});
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}

	@Override
	public void initListener() {

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
