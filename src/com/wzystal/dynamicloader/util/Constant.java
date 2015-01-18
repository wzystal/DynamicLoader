package com.wzystal.dynamicloader.util;

import android.os.Environment;

public class Constant {
	public static final String TAG = "wzy";
	public static final String DIR_PLUGINS = Environment
			.getExternalStorageDirectory() + "/plugins/";
	public static final String ACTION_GRIDVIEW_PLUGINS = "com.wzystal.dynamicloader.ACTION_GRIDVIEW_PLUGINS";
	public static final String EXTRA_PLUGIN_PATH = "com.wzystal.dynamicloader.EXTRA_PLUGIN_PATH";
	public static final String EXTRA_PLUGIN_NAME = "com.wzystal.dynamicloader.EXTRA_PLUGIN_NAME";
	public static final String EXTRA_PACKAGE_NAME = "com.wzystal.dynamicloader.EXTRA_PACKAGE_NAME";
	public static final String EXTRA_LAUNCHER_ACTIVITY = "com.wzystal.dynamicloader.EXTRA_LAUNCHER_ACTIVITY";

	public static final String NFS_MOUNT = "mount -t nfs";
	public static final String NFS_PARAMS = "-o nolock";
	public static final String NFS_DIR = "192.168.1.66:/home/wzystal/nfs/apps";
	public static final String LOCAL_DIR = Environment.getExternalStorageDirectory() + "/apps";
}
