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
	public static final String CLASS_NAME = "PluginsWidgetProvider";
	public static final String ACTION_PLUGIN = "com.wzystal.dynamicloader.ACTION_PLUGIN";
	public static final String EXTRA_PLUGIN = "com.wzystal.dynamicloader.EXTRA_PLUGIN";
	public static final String ACTION_PLUGIN_CLICK = "com.wzystal.dynamicloader.ACTION_PLUGIN_CLICK";

	// 每个请求都会传递给onReceive方法，该方法根据Intent的action类型来决定自己处理还是分发给其他方法
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		LogHelper.d(TAG, CLASS_NAME + ".onReceive() called!");
		String action = intent.getAction();
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
		if (action.equals(ACTION_PLUGIN_CLICK)) {

		}
	}

	// 到达更新时间或者用户向桌面添加widget实例时调用。
	// 注：一个widget可以生成多个实例，appWidgetIds中存储了这些实例的id，当widget更新时，所有实例同时更新！
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		System.out.println(CLASS_NAME + ".onUpdate() called!");
		LogHelper.d(TAG, CLASS_NAME + ".onUpdate() called!");
		for (int widgetId : appWidgetIds) {
			// AppWidget中的视图及其绑定事件，都是通过RemoteViews来管理的
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_plugins);
			// PluginsWidgetService继承自RemoteViewsService，用于管理复杂视图（类似Adapter）
			Intent serviceIntent = new Intent(context,
					PluginsWidgetService.class);
			// RemoteViews将复杂视图与其对应的Adapter绑定在一起
			remoteViews.setRemoteAdapter(R.id.gridview_plugins, serviceIntent);
			// 更新AppWidget管理器
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
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
