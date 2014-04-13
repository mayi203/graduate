package mayi.lagou.com.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import mayi.lagou.com.LaGouApi;
import mayi.lagou.com.R;
import mayi.lagou.com.activity.HomeActivity;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.data.PositionDetail;
import mayi.lagou.com.fragment.JobFragment.OnChangeUrl;
import mayi.lagou.com.utils.ParserUtil;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.view.MyDialog;
import mayi.lagou.com.widget.networkdialog.DialogUtils;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class JobDetailFragment extends BaseFragment {

	private TextView title, require, release_time, com_name, com_del, details;
	private String mUrl, token;
	private ImageView com_img;
	private View view;
	private View deliver;
	private OnChangeUrl onChangeUrl;
	private boolean config = false;
	private MyDialog myDialog;

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
		require = findTextView(R.id.require);
		release_time = findTextView(R.id.release_time);
		com_name = findTextView(R.id.com_name);
		com_del = findTextView(R.id.com_del);
		com_img = findImageView(R.id.com_img);
		details = findTextView(R.id.details);
		deliver = findViewById(R.id.lay_deliver);
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
		deliver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getUserInfo();
			}
		});
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
	}

	private void refreshData() {
		DialogUtils.showProcessDialog(getActivity(), true);
		client.get(mUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				PositionDetail detail = ParserUtil
						.parserPositionDetail(response);
				title.setText(detail.getPositionName());
				require.setText(detail.getSalary() + "/" + detail.getCity()
						+ "/" + detail.getExperience() + "/"
						+ detail.getEducation() + "/" + detail.getJobCategory()
						+ "\n" + detail.getJobTempt().trim());
				release_time.setText(detail.getReleaseTime());
				app().getImageLoader().loadImage(com_img,
						detail.getComIconUrl(), R.drawable.waiting);
				com_name.setText(detail.getCompany().trim());
				com_del.setText(detail.getField().trim() + "|"
						+ detail.getScale() + "|" + detail.getStage() + "\n"
						+ "地址：" + detail.getAddress());
				details.setText(detail.getJobDetail());
				DialogUtils.hideProcessDialog();
				view.setVisibility(View.VISIBLE);
				token = detail.getSubmitValue();
				if (config) {
					confirmation();
				}
			}
		});
	}

	@Override
	public void onDestroyView() {
		DialogUtils.hideProcessDialog();
		super.onDestroyView();
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
								deliverResume();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void deliverResume() {
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
							if ("20".equals(code) || "21".equals(code)) {
								showDialog(message);
								config = true;
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
		myDialog = new MyDialog(getActivity(), message);
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
							String code = object.optString("code");
							String message = object.optString("msg");
							if ("20".equals(code) || "21".equals(code)) {
								Toast.makeText(getActivity(), message,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.v("Lagou", content);
					}
				});
	}
}
