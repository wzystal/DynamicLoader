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
		case FileObserver.CREATE:
			LogHelper.d(TAG, "FileObserver.CREATE");
		case FileObserver.MOVED_FROM:
			LogHelper.d(TAG, "FileObserver.MOVED_FROM");
		case FileObserver.DELETE_SELF:
			LogHelper.d(TAG, "FileObserver.DELETE_SELF");
		case FileObserver.DELETE:
			LogHelper.d(TAG, "FileObserver.DELETE");
			mWidgetManager.notifyAppWidgetViewDataChanged(mWidgetId, R.id.gridview_plugins);
			break;
		default:
			break;
		}
	}
}
