package mayi.lagou.com.activity;

import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragmentActivity;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;
import mayi.lagou.com.fragment.LoginFragment;
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.fragment.UserInfoFragment;
import mayi.lagou.com.fragment.UserInfoFragment.OnRequestInfo;
import mayi.lagou.com.utils.SharePreferenceUtil;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class UserInfoActicity extends BaseFragmentActivity implements Refresh,
		OnRequestInfo, OnChangeUrl {

	private UserInfo mInfo;
	private String mUrl;

	@Override
	public int contentView() {
		return R.layout.a_user_info;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {
		if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            FrameLayout main=(FrameLayout)findViewById(R.id.u_contain);
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
		refresh();
	}

	@Override
	public void initListener() {

	}

	@Override
	public void refresh() {
		String email = SharePreferenceUtil.getString(this, "email");
		if (email == null || "".equals(email)) {
			changeFragment(new LoginFragment(), R.id.u_contain);
		} else {
			changeFragment(new UserInfoFragment(), R.id.u_contain);
		}
	}

	@Override
	public void setUserInfo(UserInfo user) {
		mInfo = user;
	}

	@Override
	public UserInfo getUserInfo() {
		return mInfo;
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

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub
		
	}

}
