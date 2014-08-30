/**
 * 
 */
package mayi.lagou.com.fragment;

import mayi.lagou.com.R;
import mayi.lagou.com.core.BaseFragment;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ShareActionProvider;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-14
 */
public class AboutUsFragment extends BaseFragment {

	@Override
	public int contentView() {
		getActivity().getActionBar().setTitle(R.string.string_about_us_btn);
		return R.layout.f_about_us;
	}

	@Override
	public void findViewsById() {

	}

	@Override
	public void initValue() {
		hideSoftInput();
	}

	@Override
	public void initListener() {

		findTextView(R.id.gmail).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse("mailto:203mayi@gmail.com"));
				data.putExtra(Intent.EXTRA_SUBJECT, "拉勾招聘使用反馈");
				startActivity(data);
			}
		});
	}

	private Intent shareUrl() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,
				"拉勾招聘可从拉勾网获取最新互联网招聘信息，随时随地投递简历，你也来试试吧！"
						+ "http://down.mumayi.com/592701");
		sendIntent.setType("text/plain");
		return sendIntent;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.jb_detail, menu);
		MenuItem item = menu.findItem(R.id.share);
		ShareActionProvider mShareActionProvider = (ShareActionProvider) item
				.getActionProvider();
		mShareActionProvider.setShareIntent(shareUrl());
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			getActivity().getActionBar().setTitle(R.string.app_setting);
			getActivity().onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
