package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;

public class UserInfoFragment extends BaseFragment {

	private TextView userInfo;
	private ImageView userHead;
	private OnRequestInfo onRequest;

	public UserInfoFragment() {

	}

	@Override
	public int contentView() {
		return R.layout.f_user_info;
	}

	@Override
	public void findViewsById() {
		userInfo = findTextView(R.id.base_info);
		userHead = findImageView(R.id.user_icon);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void initListener() {
		findTextView(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		findViewById(R.id.lay_resume).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFragmentToStack(R.id.u_contain, new MyResumeFragment());
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onRequest = (UserInfoActicity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		String content = SharePreferenceUtil.getString(getActivity(),
				"userInfo");
		if (content != null && !"".equals(content)) {
			initData(content);
		}
		if (NetWorkState.isNetWorkConnected(getActivity())) {
			getUserInfo();
		} else {
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
					public void onSuccess(int statusCode, String response) {
						if (statusCode == 200) {
							try {
								JSONObject object = new JSONObject(response);
								SharePreferenceUtil.putString(getActivity(),
										"userId",
										object.optJSONObject("content")
												.optString("userid"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							getResume(LaGouApi.Host + LaGouApi.Resume);
						}
					}
				});
	}

	private void getResume(String url) {
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				initData(content);
				SharePreferenceUtil.putString(getActivity(), "userInfo",
						content);
				Log.v("lagou", content);
			}
		});
	}

	private void initData(String content) {
		UserInfo user = ParserUtil.parserUserInfo(content);
		onRequest.setUserInfo(user);
		userInfo.setText(user.getBasicInfo());
		if (user.getUserIcon() != null && !"".equals(user.getUserIcon())&&!isExit) {
			app().getImageLoader().loadImage(userHead, user.getUserIcon(),
					R.drawable.default_avatar);
		}
	}

	public interface OnRequestInfo {
		public void setUserInfo(UserInfo user);

		public UserInfo getUserInfo();
	}

	private boolean isExit=false;
	@Override
	public void onPause() {
		isExit=true;
		super.onPause();
	}
	
}
