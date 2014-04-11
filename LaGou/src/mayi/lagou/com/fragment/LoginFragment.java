package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;

public class LoginFragment extends BaseFragment implements OnClickListener {

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
	public void onResume() {
		super.onResume();
		getUserInfo();
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "wenfeili@163.com");
		map.put("password", "l1w2f3");
		map.put("autoLogin", "1");
		map.put("callback", "");
		map.put("authType", "");
		map.put("signature", "");
		map.put("timestamp", "");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.LogIn, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						getResume(LaGouApi.Host + LaGouApi.Resume);
					}
				});
	}

	private void getResume(String url) {
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				Log.v("lagou", response);
			}
		});
	}
}
