package com.wzystal.dynamicloader;

import java.io.File;
import java.util.ArrayList;

import com.ryg.utils.DLUtils;
import com.wzystal.dynamicloader.R;
import com.wzystal.dynamicloader.util.DLHelper;
import com.wzystal.dynamicloader.util.LogHelper;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import static com.wzystal.dynamicloader.util.Constant.*;

public class PluginsWidgetService extends RemoteViewsService {
	private static final String CLASS_NAME = "PluginsWidgetService";

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new GridRemoteViewsFactory(this, intent);
	}

	private class GridRemoteViewsFactory implements RemoteViewsFactory {
		private Context mContext;
		private int mWidgetId;
		private ArrayList<Plugin> data = new ArrayList<Plugin>();

		public GridRemoteViewsFactory(Context context, Intent intent) {
			mContext = context;
			mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			LogHelper.d(TAG, "GridRemoteViewsFactory.mWidgetId:" + mWidgetId);
		}

		@Override
		public void onCreate() {
			LogHelper.d(TAG, CLASS_NAME + ".onCreate() called!");
			// new PluginsInitTask().execute();
			// // 读取应用信息
			// File dir = new File(DIR_PLUGINS);
			// File[] plugins = dir.listFiles();
			// if (plugins == null || plugins.length == 0) {
			// return;
			// }
			// for (File file : plugins) {
			// if (file.getName().endsWith(".apk")) {
			// Plugin plugin = new Plugin(mContext,
			// file.getAbsolutePath());
			// data.add(plugin);
			// }
			// }
		}

		// 异步任务--获取DIR_PLUGIN目录下的应用信息
		class PluginsInitTask extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... params) {
				File dir = new File(DIR_PLUGINS);
				File[] plugins = dir.listFiles();
				if (plugins == null || plugins.length == 0) {
					return null;
				}
				for (File file : plugins) {
					if (file.getName().endsWith(".apk")) {
						Plugin plugin = new Plugin(mContext,
								file.getAbsolutePath());
						data.add(plugin);
					}
				}
				return null;
			}
		}

		@Override
		public RemoteViews getViewAt(int position) {
			RemoteViews rv = new RemoteViews(mContext.getPackageName(),
					R.layout.gridview_item_plugin);
			Plugin plugin = data.get(position);
			rv.setImageViewBitmap(R.id.iv_plugin_icon, DLHelper
					.drawable2Bitmap(DLUtils.getAppIcon(mContext,
							plugin.getPluginPath())));
			rv.setTextViewText(R.id.tv_plugin_name, plugin.getPluginName());
			Intent intent = new Intent();
			intent.putExtra(PluginsWidgetProvider.EXTRA_PLUGIN, position);
			rv.setOnClickFillInIntent(R.id.gridview_plugins, intent);
			return rv;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public RemoteViews getLoadingView() {
			return null;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public void onDataSetChanged() {
			LogHelper.d(TAG, CLASS_NAME + ".onDataSetChanged() called!");
			// 读取应用信息
			if (data.size() > 0)
				data.clear();
			File dir = new File(DIR_PLUGINS);
			File[] plugins = dir.listFiles();
			if (plugins == null || plugins.length == 0) {
				return;
			}
			for (File file : plugins) {
				if (file.getName().endsWith(".apk")) {
					Plugin plugin = new Plugin(mContext, file.getAbsolutePath());
					data.add(plugin);
				}
			}
		}

		@Override
		public void onDestroy() {
			data.clear();
		}
	}
}
