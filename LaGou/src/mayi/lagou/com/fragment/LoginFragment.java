package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class LoginFragment extends BaseFragment implements OnClickListener {

	private Refresh refresh;

	@Override
	public int contentView() {
		return R.layout.f_login;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {
		findTextView(R.id.back).setOnClickListener(this);
		findButton(R.id.login_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getActivity().onBackPressed();
			break;
		case R.id.login_btn:
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		refresh = (UserInfoActicity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(NetWorkState.isNetWorkConnected(getActivity())){
			getUserInfo();
		}else{
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
		}
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "wenfeili@163.com");
		map.put("password", "l1w2f3");
		map.put("autoLogin", "1");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.LogIn, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						if(statusCode==200){
							SharePreferenceUtil.putString(getActivity(), "email",
									"wenfeili@163.com");
							SharePreferenceUtil.putString(getActivity(), "psw", "l1w2f3");
							refresh.refresh();
						}
					}
				});
	}

	public interface Refresh {
		public void refresh();
	}
}
