package com.wzystal.dynamicloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.ryg.utils.DLUtils;
import com.wzystal.dynamicloader.R;
import com.wzystal.dynamicloader.util.DLHelper;
import com.wzystal.dynamicloader.util.LogHelper;

import eu.chainfire.libsuperuser.Shell;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
		private File pluginsDir;
		private ArrayList<Plugin> data = new ArrayList<Plugin>();

		public GridRemoteViewsFactory(Context context, Intent intent) {
			pluginsDir = new File(LOCAL_DIR);
			mContext = context;
			mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			LogHelper.d(TAG, "GridRemoteViewsFactory.mWidgetId:" + mWidgetId);
		}

		@Override
		public void onCreate() {
			LogHelper.d(TAG, CLASS_NAME + ".onCreate() called!");
			//挂载NFS目录
			String cmd = NFS_MOUNT + " " + NFS_DIR + " " + LOCAL_DIR + " " + NFS_PARAMS;
			new MountTask().execute(cmd);
		}

		private class MountTask extends AsyncTask<String, Void, Void>{
			@Override
			protected Void doInBackground(String...list) {
				String cmd = list[0];
				Log.d(TAG, "是否获得ROOT权限：" + (Shell.SU.available() ? "是" : "否"));
				Log.d(TAG, "开始执行shell命令： \n" + cmd);
				List<String> suResult = Shell.SU.run(cmd);
				Log.d(TAG, "执行结果： \n" + suResult.toString());
				return null;
			}
		}
		
		@Override
		public void onDataSetChanged() {
			LogHelper.d(TAG, CLASS_NAME + ".onDataSetChanged() called!");
			initData();
		}

		private void initData() {
			// 读取应用信息
			if (data.size() > 0)
				data.clear();
			File[] plugins = pluginsDir.listFiles();
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
		public RemoteViews getViewAt(int position) {
			RemoteViews rv = new RemoteViews(mContext.getPackageName(),
					R.layout.gridview_item_plugins);
			Plugin plugin = data.get(position);
			rv.setImageViewBitmap(R.id.iv_plugin_icon, DLHelper
					.drawable2Bitmap(DLUtils.getAppIcon(mContext,
							plugin.getPluginPath())));
			rv.setTextViewText(R.id.tv_plugin_name, plugin.getPluginName());
			// 设置第position位的“视图”对应的响应事件
			Intent intent = new Intent();
			intent.putExtra(EXTRA_PLUGIN_PATH, plugin.getPluginPath());
			intent.putExtra(EXTRA_PLUGIN_NAME, plugin.getPluginName());
			intent.putExtra(EXTRA_PACKAGE_NAME, plugin.getPackageName());
			intent.putExtra(EXTRA_LAUNCHER_ACTIVITY,
					plugin.getLauncherActivity());
			rv.setOnClickFillInIntent(R.id.gridview_item_plugins, intent);
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
		public void onDestroy() {
			data.clear();
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
	}
}
