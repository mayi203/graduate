/**
 * 
 */
package mayi.lagou.com.adapter;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.R;
import mayi.lagou.com.data.LaGouPosition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-3-31
 */
public class JobItemAdapt extends BaseAdapter {
	private Context context;

	public JobItemAdapt(Context mContext) {
		this.context = mContext;
		objects = new ArrayList<LaGouPosition>();
	}

	static class ViewHolder {
		private TextView position;
		// private TextView address;
		private TextView compony;
		private TextView salary;
		private TextView education;
		private TextView exprience;
		private TextView time;
	}

	protected List<LaGouPosition> objects;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return objects.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return objects.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder holder;
		if (convertview == null) {
			convertview = LayoutInflater.from(context).inflate(
					R.layout.item_job, null);
			holder = new ViewHolder();
			// holder.address = (TextView)
			// convertview.findViewById(R.id.address);
			holder.position = (TextView) convertview
					.findViewById(R.id.position);
			holder.compony = (TextView) convertview.findViewById(R.id.compony);
			holder.salary = (TextView) convertview.findViewById(R.id.salary);
			holder.education = (TextView) convertview
					.findViewById(R.id.education);
			holder.exprience = (TextView) convertview
					.findViewById(R.id.exprience);
			holder.time = (TextView) convertview.findViewById(R.id.time);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.position.setText(objects.get(position).getPositionName());
		holder.compony.setText(objects.get(position).getCompany());
		holder.salary.setText(objects.get(position).getMoney()
				.substring(objects.get(position).getMoney().indexOf("：") + 1)
				+ "   |");
		holder.education.setText(objects.get(position).getCity()
				.replace("[", "").replace("]", "")
				+ "   |");
		holder.exprience.setText(objects.get(position).getCompany());
		holder.time.setText(objects.get(position).getTime().replace("发布", ""));
		// holder.address.setText(objects.get(position).getCity());
		// holder.education.setText(objects.get(position).getEducation()
		// .substring(objects.get(position).getEducation().indexOf("："))
		// .replace("：", "|"));
		// holder.exprience.setText(objects.get(position).getExperience()
		// .substring(objects.get(position).getExperience().indexOf("："))
		// .replace("：", "|"));
		return convertview;
	}

	public void addItems(List<LaGouPosition> items) {
		if (items != null) {
			objects.addAll(items);
			notifyDataSetChanged();
		}
	}

	public void deleteItems(List<LaGouPosition> items) {
		objects.removeAll(items);
		notifyDataSetChanged();
	}

	public void deleteAllItems() {
		objects.clear();
		notifyDataSetChanged();
	}

}
