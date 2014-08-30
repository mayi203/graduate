package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.UserInfo;
import mayi.lagou.com.fragment.LoginFragment.Refresh;
import mayi.lagou.com.utils.NetWorkState;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.view.MyDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.fb.FeedbackAgent;

/**
 * @author 203mayi@gmail.com 2014-5-6
 */
public class UserInfoFragment extends BaseFragment implements OnClickListener {

	private TextView userInfo, resume, deliver, deliverSet, changeUser,
			umengFb;
	private ImageView userHead;
	private OnRequestInfo onRequest;
	private Refresh refresh;

	public UserInfoFragment() {

	}

	@Override
	public int contentView() {
		getActivity().getActionBar().setTitle(R.string.self);
		return R.layout.f_user_info;
	}

	@Override
	public void findViewsById() {
		userInfo = findTextView(R.id.base_info);
		userHead = findImageView(R.id.user_icon);
		resume = findTextView(R.id.my_resume);
		deliver = findTextView(R.id.my_deliver);
		deliverSet = findTextView(R.id.deliver_set);
		changeUser = findTextView(R.id.change_user);
		umengFb = findTextView(R.id.umeng_fb);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void initListener() {
		resume.setOnClickListener(this);
		deliver.setOnClickListener(this);
		deliverSet.setOnClickListener(this);
		changeUser.setOnClickListener(this);
		umengFb.setOnClickListener(this);
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
								String success = object.optString("success");
								if ("true".equals(success) && !isExit) {
									SharePreferenceUtil.putString(
											getActivity(), "userId", object
													.optJSONObject("content")
													.optString("userid"));
									getResume(LaGouApi.Host + LaGouApi.Resume);
								} else if ("false".equals(success) && !isExit) {
									logOut();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
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
		case R.id.my_resume:
			addFragmentToStack(R.id.u_contain, new MyResumeFragment());
			break;
		case R.id.my_deliver:
			addFragmentToStack(R.id.u_contain, new DeliverFeedbackFragment());
			// getMyDeliver();
			break;
		case R.id.change_user:
			affriLogout();
			break;
		case R.id.deliver_set:
			deliverSet();
			break;
		case R.id.umeng_fb:
			FeedbackAgent agent = new FeedbackAgent(getActivity());
			agent.startFeedbackActivity();
		default:
			break;
		}
	}

	private MyDialog dialog;

	private void affriLogout() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = new MyDialog(getActivity());
		dialog.show();
		dialog.setMessage("退出将清除个人信息，确定要退出吗？");
		dialog.setOkBtnText("确定");
		dialog.setOkBtnOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logOut();
				dialog.dismiss();
			}
		});
		dialog.setCancelBtnText("取消");
		dialog.showDefaultCancelBtn();
	}

	private void logOut() {
		SharePreferenceUtil.putString(getActivity(), "email", "");
		SharePreferenceUtil.putString(getActivity(), "psw", "");
		SharePreferenceUtil.putString(getActivity(), "userInfo", null);
		SharePreferenceUtil.putString(getActivity(),
				SharePreferenceUtil.RESUME_TYPE, "");
		refresh.refresh();
	}

	private void deliverSet() {
		int type = 1;
		String resumeType = SharePreferenceUtil.getString(getActivity(),
				SharePreferenceUtil.RESUME_TYPE);
		if (resumeType != null && !"".equals(resumeType)) {
			type = Integer.parseInt(resumeType);
		}
		new AlertDialog.Builder(getActivity())
				.setTitle("选择默认投递简历")
				.setSingleChoiceItems(new String[] { "附件简历", "在线简历" }, type,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								SharePreferenceUtil.putString(getActivity(),
										SharePreferenceUtil.RESUME_TYPE,
										String.valueOf(which));
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			getActivity().getActionBar().setTitle(R.string.app_name);
			getActivity().onBackPressed();
		} else if (item.getItemId() == R.id.about_us) {
			addFragmentToStack(R.id.u_contain, new AboutUsFragment());
		}
		return super.onOptionsItemSelected(item);
	}

}
