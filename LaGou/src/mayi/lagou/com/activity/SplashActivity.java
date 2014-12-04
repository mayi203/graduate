package mayi.lagou.com.activity;

import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends BaseActivity {

	private TextView lagou, lagouDel;

	@Override
	public int contentView() {
		return R.layout.a_splash;
	}

	@Override
	public void findViewsById() {
		if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            RelativeLayout main=(RelativeLayout)findViewById(R.id.main);
            main.setPadding(0, LaGouApp.getInstance().getStatusBarHeight(), 0, 0);
         // 创建TextView
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LaGouApp.getInstance().getStatusBarHeight());
            textView.setBackgroundColor(Color.parseColor("#019875"));
            textView.setLayoutParams(lParams);
            // 获得根视图并把TextView加进去。
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.addView(textView);
        }
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
				intent.setClass(SplashActivity.this, HomeActivity.class);
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
