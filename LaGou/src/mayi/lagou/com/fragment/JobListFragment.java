package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class JobListFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    public static JobListFragment newInstance(String content) {
        JobListFragment fragment = new JobListFragment();
        fragment.mContent = content;

        return fragment;
    }

    private String mContent = "???";
    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @SuppressLint("InflateParams")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View main=inflater.inflate(R.layout.f_job_list, null);
    	text=(TextView)main.findViewById(R.id.txt);
    	text.setText(mContent);
        return main;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
