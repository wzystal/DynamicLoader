package com.wzystal.dynamicloader.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class AppsWidgetService extends RemoteViewsService{
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return null;
	}
	
	class GridViewsFactory implements RemoteViewsFactory{
		
	}
}
