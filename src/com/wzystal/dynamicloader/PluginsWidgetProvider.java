package com.wzystal.dynamicloader;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wzystal.dynamicloader.R;
import com.wzystal.dynamicloader.util.LogHelper;

import static com.wzystal.dynamicloader.util.Constant.*;

public class PluginsWidgetProvider extends AppWidgetProvider {
	public static final String CLASS_NAME = "PluginsWidgetProvider";

	// 每个请求都会传递给onReceive方法，该方法根据Intent的action类型来决定自己处理还是分发给其他方法
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		LogHelper.d(
				TAG,
				CLASS_NAME + ".onReceive() called! Action="
						+ intent.getAction());
		if (action.equals(ACTION_GRIDVIEW_PLUGINS)) {
			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			String pluginName = intent.getStringExtra(EXTRA_PLUGIN_NAME);
			Toast.makeText(context, "正在加载 " + pluginName + "...",
					Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	// 到达更新时间或者用户向桌面添加widget实例时调用。
	// 注：一个widget可以生成多个实例，appWidgetIds中存储了这些实例的id，当widget更新时，所有实例同时更新！
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		LogHelper.d(TAG, CLASS_NAME + ".onUpdate() called!");
		for (int widgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_plugins);
			Intent serviceIntent = new Intent(context,
					PluginsWidgetService.class);
			remoteViews.setRemoteAdapter(R.id.gridview_plugins, serviceIntent);

			/**
			 * 一般使用setOnClickPendingIntent方法来设置一个控件的点击事件。 但对于复杂视图中的子项，
			 * 需要先用setPendingIntentTemplate方法为复杂试图整体的点击事件设置一个处理的PendingIntent，
			 * 然后通过RemoteViewsFactory使用setOnClickFillInIntent为复杂视图中的每一项传入一个与该项相关的Intent
			 * 。 该Intent会被合入处理时接收到Intent中。
			 */
			Intent gridIntent = new Intent();
			gridIntent.setAction(ACTION_GRIDVIEW_PLUGINS);
			gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setPendingIntentTemplate(R.id.gridview_plugins,
					pendingIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);

			PluginsObserver pluginsObserver = new PluginsObserver(DIR_PLUGINS,
					appWidgetManager, widgetId);
			pluginsObserver.startWatching();
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	// widget从桌面移除时调用
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		LogHelper.d(TAG, CLASS_NAME + ".onDeleted() called!");
		super.onDeleted(context, appWidgetIds);
	}

	// widget的第一个实例被添加到桌面时调用
	@Override
	public void onEnabled(Context context) {
		LogHelper.d(TAG, CLASS_NAME + ".onEnabled() called!");
		super.onEnabled(context);
	}

	// widget的最后一个实例从桌面移除时调用
	@Override
	public void onDisabled(Context context) {
		LogHelper.d(TAG, CLASS_NAME + ".onDisabled() called!");
		super.onDisabled(context);
	}
}
