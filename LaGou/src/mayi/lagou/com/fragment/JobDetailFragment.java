package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.HomeActivity;
import mayi.lagou.com.activity.UserInfoActicity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.PositionDetail;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.view.MyDialog;
import mayi.lagou.com.widget.networkdialog.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class JobDetailFragment extends BaseFragment {

	private TextView title, require, release_time, com_name, com_del, details,
			bottomTxt;
	private String mUrl, token;
	private ImageView com_img;
	private RelativeLayout detail_null;
	private View view;
	private View deliver;
	private OnChangeUrl onChangeUrl;
	private boolean config = false;
	private MyDialog myDialog;
	private Animation anim;
	private ImageView email;
	private Boolean isLogin = true;

	public JobDetailFragment() {

	}

	@Override
	public int contentView() {
		return R.layout.f_job_detail;
	}

	@Override
	public void findViewsById() {
		view = findViewById(R.id.scroll);
		title = findTextView(R.id.title);
		detail_null = (RelativeLayout) findViewById(R.id.job_detail_null);
		require = findTextView(R.id.require);
		release_time = findTextView(R.id.release_time);
		com_name = findTextView(R.id.com_name);
		com_del = findTextView(R.id.com_del);
		com_img = findImageView(R.id.com_img);
		details = findTextView(R.id.details);
		deliver = findViewById(R.id.lay_deliver);
		email = findImageView(R.id.email);
		bottomTxt = findTextView(R.id.bottom_txt);
	}

	@Override
	public void initValue() {
		anim = AnimationUtils.loadAnimation(getActivity(), R.anim.email_anim);
		anim.setRepeatMode(Animation.RESTART);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				email.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void initListener() {
		findTextView(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		deliver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLogin) {
					getUserInfo();
				} else {
					startActivity(UserInfoActicity.class);
					SharePreferenceUtil.putBoolean(getActivity(), "toDetail",
							true);
				}
			}
		});
		findImageView(R.id.share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareUrl(mUrl);
			}
		});
	}

	private void shareUrl(String url) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "我在拉钩网客户端发现了一个很不错的职位" + url
				+ "你也去看看吧！");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onChangeUrl = (HomeActivity) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		mUrl = onChangeUrl.getUrl();
		refreshData();
		String userInfo = SharePreferenceUtil.getString(getActivity(), "email");
		if (userInfo == null || "".equals(userInfo)) {
			bottomTxt.setText("投个简历[未登录]");
			isLogin = false;
		} else {
			bottomTxt.setText("投个简历");
			isLogin = true;
		}
	}

	private void refreshData() {
		DialogUtils.showProcessDialog(getActivity(), true);
		client.get(mUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				PositionDetail detail = ParserUtil
						.parserPositionDetail(response);
				if (detail != null && !"".equals(detail)) {
					view.setVisibility(View.VISIBLE);
					deliver.setVisibility(View.VISIBLE);
					title.setText(detail.getPositionName());
					require.setText(detail.getSalary() + "/" + detail.getCity()
							+ "/" + detail.getExperience() + "/"
							+ detail.getEducation() + "/"
							+ detail.getJobCategory() + "\n"
							+ detail.getJobTempt().trim());
					release_time.setText(detail.getReleaseTime());
					if (detail.getComIconUrl() != null
							&& !"".equals(detail.getComIconUrl()) && !isExit) {
						app().getImageLoader().loadImage(com_img,
								detail.getComIconUrl(), R.drawable.waiting);
					}
					com_name.setText(detail.getCompany().trim());
					com_del.setText(detail.getField().trim() + "|"
							+ detail.getScale() + "|" + detail.getStage()
							+ "\n" + "地址：" + detail.getAddress());
					details.setText(detail.getJobDetail());
					token = detail.getSubmitValue();
					if (config) {
						confirmation();
					}
				} else {
					detail_null.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFinish() {
				DialogUtils.hideProcessDialog();
				super.onFinish();
			}
		});
	}

	@Override
	public void onDestroyView() {
		DialogUtils.hideProcessDialog();
		super.onDestroyView();
	}

	private void startAnim() {
		email.setVisibility(View.VISIBLE);
		email.setAnimation(anim);
		anim.start();
	}

	private void getUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", SharePreferenceUtil.getString(getActivity(), "email"));
		map.put("password", SharePreferenceUtil.getString(getActivity(), "psw"));
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
								deliverResume();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void deliverResume() {
		startAnim();
		String userId = SharePreferenceUtil.getString(getActivity(), "userId");
		String jobId = mUrl.substring(mUrl.length() - 9, mUrl.length() - 5);
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("positionId", jobId);
		map.put("resubmitToken", token);
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.Deliver, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, String content) {
						try {
							JSONObject object = new JSONObject(content);
							String code = object.optString("code");
							String message = object.optString("msg");
							String success = object.optString("success");
							if ("20".equals(code) || "21".equals(code)) {
								showDialog(message);
								config = true;
							} else if (!"true".equals(success)) {
								Toast.makeText(getActivity(),
										"投递失败：" + message, Toast.LENGTH_SHORT)
										.show();
							} else if ("true".equals(success)) {
								Toast.makeText(getActivity(), "投递成功！",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.v("Lagou", content);
					}
				});
	}

	private void showDialog(String message) {
		if (myDialog != null && myDialog.isShowing()) {
			myDialog.dismiss();
		}
		String mMessage = ParserUtil.parseResumeDialogText(message);
		myDialog = new MyDialog(getActivity(), mMessage);
		myDialog.show();
		myDialog.setOkBtnOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshData();
				myDialog.dismiss();
			}
		});
		myDialog.setCancelBtnOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
	}

	private void confirmation() {
		String userId = SharePreferenceUtil.getString(getActivity(), "userId");
		String jobId = mUrl.substring(mUrl.length() - 9, mUrl.length() - 5);
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("positionId", jobId);
		map.put("resubmitToken", token);
		map.put("force", "true");
		map.put("type", "0");
		RequestParams params = new RequestParams(map);
		client.post(LaGouApi.Host + LaGouApi.Deliver, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, String content) {
						try {
							JSONObject object = new JSONObject(content);
							String message = object.optString("msg");
							String success = object.optString("success");
							if (!"true".equals(success)) {
								Toast.makeText(getActivity(),
										"投递失败：" + message, Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(getActivity(), "投递成功！",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.v("Lagou", content);
					}
				});
	}

	private boolean isExit = false;

	@Override
	public void onPause() {
		isExit = true;
		super.onPause();
	}
}
