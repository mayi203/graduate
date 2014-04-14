package mayi.lagou.com.adapter;

import java.util.List;

import mayi.lagou.com.R;
import mayi.lagou.com.data.JobExperience;
import mayi.lagou.com.imageloader.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExprienceAdapter extends BaseAdapter {
	/**
	 * @author wenfeili@163.com
	 * 
	 * @date 2014-3-31
	 */
	private Context context;
	private ImageLoader mLoader;

	public ExprienceAdapter(Context mContext, ImageLoader loader,
			List<JobExperience> object) {
		this.context = mContext;
		mLoader = loader;
		objects = object;
	}

	static class ViewHolder {
		private TextView exp_time;
		private TextView exp_name;
		private TextView exp_job;
		private ImageView exp_icon;
	}

	protected List<JobExperience> objects;

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int arg0) {
		return objects.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder holder;
		if (convertview == null) {
			convertview = LayoutInflater.from(context).inflate(
					R.layout.item_exprience, null);
			holder = new ViewHolder();
			holder.exp_time = (TextView) convertview
					.findViewById(R.id.exp_time);
			holder.exp_job = (TextView) convertview.findViewById(R.id.exp_job);
			holder.exp_name = (TextView) convertview
					.findViewById(R.id.exp_name);
			holder.exp_icon = (ImageView) convertview
					.findViewById(R.id.exp_icon);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.exp_time.setText(objects.get(position).getJobTime());
		holder.exp_name.setText(objects.get(position).getCompanyName());
		holder.exp_job.setText(objects.get(position).getPositionName());
		mLoader.loadImage(holder.exp_icon, objects.get(position).getIconUrl(),
				R.drawable.waiting);
		return convertview;
	}
}
