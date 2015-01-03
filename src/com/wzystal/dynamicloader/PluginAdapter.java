package com.wzystal.dynamicloader;

import java.util.ArrayList;
import com.ryg.utils.DLUtils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PluginAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Plugin> mItemList;

	public PluginAdapter(Context context, ArrayList<Plugin> itemList) {
		this.mContext = context;
		this.mItemList = itemList;
	}

	private static class ViewHolder {
		public ImageView appIcon;
		public TextView appName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = View.inflate(mContext, R.layout.gridview_item_plugins, null);
			holder = new ViewHolder();
			holder.appIcon = (ImageView) convertView
					.findViewById(R.id.iv_plugin_icon);
			holder.appName = (TextView) convertView
					.findViewById(R.id.tv_plugin_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Plugin item = mItemList.get(position);
		if (null != item) {
			holder.appIcon.setImageDrawable(DLUtils.getAppIcon(mContext,
					item.getPluginPath()));
			holder.appName.setText(DLUtils.getAppLabel(mContext,
					item.getPluginPath()));
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
