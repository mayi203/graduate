package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class LoginFragment extends BaseFragment implements OnClickListener {

	private Refresh refresh;
	private TextView email, psw;

	@Override
	public int contentView() {
//		getActivity().getActionBar().setTitle(R.string.string_login);
		return R.layout.f_login;
	}

	@Override
	public void findViewsById() {
		email = findTextView(R.id.email);
		psw = findTextView(R.id.psw);
	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {
		findButton(R.id.login_btn).setOnClickListener(this);
		findTextView(R.id.back_login).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			getUserInfo();
			break;
		case R.id.back_login:
			getActivity().onBackPressed();
			break;
		default:
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
		if (NetWorkState.isNetWorkConnected(getActivity())) {
		} else {
			Toast.makeText(getActivity(), "好像没有联网哦", Toast.LENGTH_SHORT).show();
		}
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		final String emailTxt = email.getText().toString().trim();
		if (emailTxt != null && !"".equals(emailTxt)) {
			map.put("email", email.getText().toString().trim());
		} else {
			Toast.makeText(getActivity(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		final String pswTxt = psw.getText().toString().trim();
		if (pswTxt != null && !"".equals(pswTxt)) {
			map.put("password", psw.getText().toString().trim());
		} else {
			Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		map.put("autoLogin", "1");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.LogIn, params,
				new AsyncHttpResponseHandler() {

					@Override
					@Deprecated
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						if (statusCode == 200) {
							try {
								JSONObject object = new JSONObject(content);
								String sucess = object.optString("success");
								String msg = object.optString("msg");
								if ("true".equals(sucess)) {
									SharePreferenceUtil.putString(
											getActivity(), "email", emailTxt);
									SharePreferenceUtil.putString(
											getActivity(), "psw", pswTxt);
									LaGouApp.isLogin=true;
									SharePreferenceUtil.putBoolean(
											getActivity(), "islogin", true);
									boolean toDetail = SharePreferenceUtil
											.getBoolean(getActivity(),
													"toDetail");
									if (toDetail) {
										SharePreferenceUtil.putBoolean(
												getActivity(), "toDetail",
												false);
										getActivity().onBackPressed();

									} else {
										refresh.refresh();
									}
								} else {
									Toast.makeText(getActivity(), msg,
											Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						super.onSuccess(statusCode, headers, content);
					}
				});
	}

	public interface Refresh {
		public void refresh();
	}

	@Override
	public void onPause() {
		super.onPause();
		hideSoftInput();
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == android.R.id.home) {
//			getActivity().getActionBar().setTitle(R.string.app_name);
//			getActivity().onBackPressed();
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
