package com.wzystal.dynamicloader.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.wzystal.dynamicloader.R;
import com.wzystal.dynamicloader.util.LogHelper;
import static com.wzystal.dynamicloader.util.Constant.*;

public class PluginsWidgetProvider extends AppWidgetProvider {
	public static final String CLASS_NAME = "AppsWidgetProvider";
	public static final String ACTION_PLUGIN_CLICK = "com.wzystal.dynamicloader.ACTION_PLUGIN_CLICK";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		LogHelper.d(LOG_TAG, CLASS_NAME + ".onUpdate() called!");
		for (int widgetId : appWidgetIds) {
			// AppWidget中的视图及其绑定事件，都是通过RemoteViews来管理的
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_apps);
			// PluginsWidgetService继承自RemoteViewsService，用于管理复杂视图（类似Adapter）
			Intent serviceIntent = new Intent(context, PluginsWidgetService.class);
			// RemoteViews将复杂视图与其对应的Adapter绑定在一起
			remoteViews.setRemoteAdapter(R.id.gridview_apps, serviceIntent);
			// 为复杂视图中的item设置响应事件绑定
			Intent itemIntent = new Intent();
			itemIntent.setAction(ACTION_PLUGIN_CLICK);
			itemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setPendingIntentTemplate(R.id.gridview_apps,
					pendingIntent);
			// 更新AppWidget管理器
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		LogHelper.d(LOG_TAG, CLASS_NAME + ".onReceive() called!");
		String action = intent.getAction();
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		if (action.equals(ACTION_ITEM_CLICK)) {
			
		}
	}
}
