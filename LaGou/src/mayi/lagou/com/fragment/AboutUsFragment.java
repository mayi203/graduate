/**
 * 
 */
package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-14
 */
public class AboutUsFragment extends BaseFragment {

	@Override
	public int contentView() {
		return R.layout.f_about_us;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {

	}

	@Override
	public void initListener() {
		findTextView(R.id.about_us_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getActivity().onBackPressed();
					}
				});
		findImageView(R.id.shareApp).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareUrl();
			}
		});
	}

	private void shareUrl() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,
				"拉勾招聘可从拉勾网获取最新互联网招聘信息，随时随地投递简历，你也来试试吧！"
						+ "http://down.mumayi.com/592701");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
}
