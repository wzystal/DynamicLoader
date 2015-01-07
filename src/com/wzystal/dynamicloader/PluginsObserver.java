package com.wzystal.dynamicloader;

import com.wzystal.dynamicloader.util.LogHelper;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import static com.wzystal.dynamicloader.util.Constant.*;

public class PluginsObserver extends FileObserver {
	private AppWidgetManager mWidgetManager;
	private int mWidgetId;

	public PluginsObserver(String path, AppWidgetManager widgetManager, int widgetId) {
		super(path);
		mWidgetManager = widgetManager;
		mWidgetId = widgetId;
	}

	@Override
	public void onEvent(int event, String path) {
		switch (event) {
		case FileObserver.MOVED_TO:
			LogHelper.d(TAG, "FileObserver.MOVED_TO");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		case FileObserver.CREATE:
			LogHelper.d(TAG, "FileObserver.CREATE");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		case FileObserver.MOVED_FROM:
			LogHelper.d(TAG, "FileObserver.MOVED_FROM");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		case FileObserver.DELETE_SELF:
			LogHelper.d(TAG, "FileObserver.DELETE_SELF");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		case FileObserver.DELETE:
			LogHelper.d(TAG, "FileObserver.DELETE");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		default:
			break;
		}
	}
	
	
}
