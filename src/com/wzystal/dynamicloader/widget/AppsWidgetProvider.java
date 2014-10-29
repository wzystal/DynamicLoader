package com.wzystal.dynamicloader.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.wzystal.dynamicloader.R;

public class AppsWidgetProvider extends AppWidgetProvider{
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for(int widgetId : appWidgetIds){
			// 获取AppWidget对应的视图
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_apps);
			
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
}
