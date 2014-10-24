package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.os.Bundle;
import android.widget.TextView;

public final class JobListFragment extends BaseFragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    TextView text;
    public static JobListFragment newInstance(String content) {
        JobListFragment fragment = new JobListFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }

    private String mContent = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        text = new TextView(getActivity());
//        text.setGravity(Gravity.CENTER);
////        text.setText(mContent);
//        text.setTextSize(20 * getResources().getDisplayMetrics().density);
//        text.setPadding(20, 20, 20, 20);
//
//        LinearLayout layout = new LinearLayout(getActivity());
//        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        layout.setGravity(Gravity.CENTER);
//        layout.addView(text);
//
//        return layout;
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

//	@Override
//	public void onResume() {
//		super.onResume();
//		text.setText(mContent);
//	}

	@Override
	public int contentView() {
		return R.layout.f_job_list;
	}

	@Override
	public void findViewsById() {
		
	}

	@Override
	public void initValue() {
		
	}

	@Override
	public void initListener() {
		
	}
    
}
