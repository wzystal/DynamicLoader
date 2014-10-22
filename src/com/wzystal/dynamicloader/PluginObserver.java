package com.wzystal.dynamicloader;

import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;

public class PluginObserver extends FileObserver {
	private Handler mHandler;

	public PluginObserver(Handler handler, String path) {
		super(path);
		this.mHandler = handler;
	}

	@Override
	public void onEvent(int event, String path) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		switch (event) {
//		case FileObserver.MOVED_TO:
		case FileObserver.CREATE:
			msg.what = FileObserver.CREATE;
			bundle.putString("pluginPath", path);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			break;
//		case FileObserver.MOVED_FROM:
//		case FileObserver.DELETE_SELF:
		case FileObserver.DELETE:
			msg.what = FileObserver.DELETE;
			bundle.putString("pluginPath", path);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			break;
		default:
			break;
		}
	}
}
