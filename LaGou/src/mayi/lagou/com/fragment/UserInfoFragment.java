package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;

/**
 * @author 203mayi@gmail.com 2014-5-6
 */
public class UserInfoFragment extends BaseFragment implements OnClickListener {

	private TextView back, userInfo, resume, aboutUs, deliver, deliverSet,
			changeUser;
	private ImageView userHead;
	private OnRequestInfo onRequest;
	private Refresh refresh;

	public UserInfoFragment() {

	}

	@Override
	public int contentView() {
		return R.layout.f_user_info;
	}

	@Override
	public void findViewsById() {
		back = findTextView(R.id.user_info_back);
		userInfo = findTextView(R.id.base_info);
		userHead = findImageView(R.id.user_icon);
		resume = findTextView(R.id.my_resume);
		aboutUs = findTextView(R.id.about_us);
		deliver = findTextView(R.id.my_deliver);
		deliverSet = findTextView(R.id.deliver_set);
		changeUser = findTextView(R.id.change_user);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void initListener() {
		back.setOnClickListener(this);
		resume.setOnClickListener(this);
		aboutUs.setOnClickListener(this);
		deliver.setOnClickListener(this);
		deliverSet.setOnClickListener(this);
		changeUser.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onRequest = (UserInfoActicity) activity;
		refresh = (UserInfoActicity) activity;
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

	private void getMyDeliver() {
		client.get(LaGouApi.Host + LaGouApi.DeliverRecord,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, String content) {
						onRequest.setDeliver(content);
						addFragmentToStack(R.id.u_contain,
								new DeliverFeedbackFragment());
					}
				});
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		String emailTxt = SharePreferenceUtil.getString(getActivity(), "email");
		map.put("email", emailTxt);
		String pswTxt = SharePreferenceUtil.getString(getActivity(), "psw");
		map.put("password", pswTxt);
		map.put("autoLogin", "1");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.LogIn, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String response) {
						if (statusCode == 200) {
							try {
								JSONObject object = new JSONObject(response);
								if (!isExit) {
									SharePreferenceUtil.putString(
											getActivity(), "userId", object
													.optJSONObject("content")
													.optString("userid"));
								}
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
				if (!isExit) {
					SharePreferenceUtil.putString(getActivity(), "userInfo",
							content);
				}
				Log.v("lagou", content);
			}
		});
	}

	private void initData(String content) {
		UserInfo user = ParserUtil.parserUserInfo(content);
		onRequest.setUserInfo(user);
		userInfo.setText(user.getBasicInfo());
		if (user.getUserIcon() != null && !"".equals(user.getUserIcon())
				&& !isExit) {
			app().getImageLoader().loadImage(userHead, user.getUserIcon(),
					R.drawable.default_avatar);
		}
	}

	public interface OnRequestInfo {
		public void setUserInfo(UserInfo user);

		public UserInfo getUserInfo();

		public void setDeliver(String deliver);

		public String getDeliver();
	}

	private boolean isExit = false;

	@Override
	public void onPause() {
		isExit = true;
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_info_back:
			getActivity().onBackPressed();
			break;
		case R.id.my_resume:
			addFragmentToStack(R.id.u_contain, new MyResumeFragment());
			break;
		case R.id.my_deliver:
			getMyDeliver();
			break;
		case R.id.change_user:
			SharePreferenceUtil.putString(getActivity(), "email", "");
			SharePreferenceUtil.putString(getActivity(), "psw", "");
			SharePreferenceUtil.putString(getActivity(), "userInfo", null);
			refresh.refresh();
			break;
		case R.id.about_us:
			addFragmentToStack(R.id.u_contain, new AboutUsFragment());
			break;
		case R.id.deliver_set:
			deliverSet();
			break;
		default:
			break;
		}
	}

	private void deliverSet() {
		int type = 0;
		String resumeType = SharePreferenceUtil.getString(getActivity(),
				SharePreferenceUtil.RESUME_TYPE);
		if (resumeType != null && !"".equals(resumeType)) {
			type = Integer.parseInt(resumeType);
		}
		new AlertDialog.Builder(getActivity())
				.setTitle("选择默认投递简历")
				.setSingleChoiceItems(new String[] { "在线简历", "附件简历" }, type,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								SharePreferenceUtil.putString(getActivity(),
										SharePreferenceUtil.RESUME_TYPE, String.valueOf(which));
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();
	}
}
