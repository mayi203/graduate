package mayi.lagou.com.fragment;

import java.io.File;

import mayi.lagou.com.LaGouApp;
import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import mayi.lagou.com.utils.SharePreferenceUtil;
import mayi.lagou.com.view.MyDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

public class SettingFragment extends BaseFragment implements OnClickListener {

	private TextView changeUser;
	@Override
	public int contentView() {
//		getActivity().getActionBar().setTitle(R.string.app_setting);
		return R.layout.f_setting;
	}

	@Override
	public void findViewsById() {
		changeUser=findTextView(R.id.change_user);
		changeUserState();
	}

	public void changeUserState(){
		if(LaGouApp.isLogin){
			changeUser.setVisibility(View.VISIBLE);
		}else{
			changeUser.setVisibility(View.GONE);
		}
	}
	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {
		findViewById(R.id.deliver_set).setOnClickListener(this);
		findViewById(R.id.umeng_fb).setOnClickListener(this);
		findViewById(R.id.clear_cache).setOnClickListener(this);
		findViewById(R.id.about_app).setOnClickListener(this);
		findViewById(R.id.change_user).setOnClickListener(this);
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == android.R.id.home) {
//			getActivity().getActionBar().setTitle(R.string.app_name);
//			getActivity().onBackPressed();
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.deliver_set:
			deliverSet();
			break;
		case R.id.umeng_fb:
			FeedbackAgent agent = new FeedbackAgent(getActivity());
			agent.startFeedbackActivity();
			break;
		case R.id.clear_cache:
			File file = new File(LaGouApp.getInstance().mSdcardDataDir);
			DeleteFile(file);
			break;
		case R.id.about_app:
			MobclickAgent.onEvent(getActivity(), "aboutus");
			addFragmentToStack(R.id.contain, new AboutUsFragment());
			break;
		case R.id.change_user:
			affriLogout();
			break;
		default:
			break;
		}
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
		File file = new File(LaGouApp.getInstance().mSdcardDataDir);
		DeleteFile(file);
		LaGouApp.isLogin = false;
		SharePreferenceUtil.putBoolean(getActivity(), "islogin", false);
		changeUserState();
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public void DeleteFile(File file) {
		SharePreferenceUtil.putString(getActivity(), "userInfo", null);
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}
}
