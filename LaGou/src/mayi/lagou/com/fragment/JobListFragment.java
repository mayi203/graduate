package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class JobListFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	TextView text;

	public static JobListFragment newInstance(String content) {
		JobListFragment fragment = new JobListFragment();
		fragment.mContent = content;

		return fragment;
	}

	private String mContent = "???";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View main = inflater.inflate(R.layout.f_job_list, null);
		text = (TextView) main.findViewById(R.id.txt);
		text.setText(mContent);
		return main;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}
	public void changeCity(String city){
		Message messge=new Message();
		messge.what=1;
		messge.obj=city;
		mHanlder.sendMessage(messge);
	}
	Handler mHanlder=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				text.setText(mContent+(String)msg.obj);
			}
		}};
}
