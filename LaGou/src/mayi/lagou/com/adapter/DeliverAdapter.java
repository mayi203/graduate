/**
 * 
 */
package mayi.lagou.com.adapter;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.R;
import mayi.lagou.com.data.DeliverFeedback;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-14
 */
public class DeliverAdapter extends BaseAdapter {

	private List<DeliverFeedback> mObjects;
	private Context mContext;

	public DeliverAdapter(Context context) {
		mObjects = new ArrayList<DeliverFeedback>();
		this.mContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mObjects.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public DeliverFeedback getItem(int position) {
		return mObjects.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_deliver_feedback, null);
			holder = new ViewHolder();
			holder.position = (TextView) convertView
					.findViewById(R.id.deliver_position);
			holder.salary = (TextView) convertView
					.findViewById(R.id.deliver_salary);
			holder.company = (TextView) convertView
					.findViewById(R.id.deliver_company);
			holder.deliverTime = (TextView) convertView
					.findViewById(R.id.deliver_time);
			holder.resume = (TextView) convertView
					.findViewById(R.id.deliver_resume);
			holder.progress = (TextView) convertView
					.findViewById(R.id.deliver_progress);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.position.setText(getItem(position).getPosition());
		holder.salary.setText(getItem(position).getSalary());
		holder.company.setText(getItem(position).getCompany());
		holder.deliverTime.setText(getItem(position).getDeliverTime());
		holder.resume.setText(getItem(position).getResume());
		holder.progress.setText(getItem(position).getProgress());
		return convertView;
	}

	static class ViewHolder {
		private TextView position;
		private TextView salary;
		private TextView company;
		private TextView deliverTime;
		private TextView resume;
		private TextView progress;
	}

	public void addItems(List<DeliverFeedback> items) {
		if (items != null) {
			mObjects.addAll(items);
			notifyDataSetChanged();
		}
	}
	public void deleteAllItems() {
		mObjects.clear();
		notifyDataSetChanged();
	}

}
