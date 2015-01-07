package com.wzystal.dynamicloader;

import com.ryg.utils.DLUtils;

import android.content.Context;
import android.content.pm.PackageInfo;

public class Plugin {
	private Context mContext;
	private String mPluginPath;// 存储路径
	private String mPluginName;// 应用名称
	private String mPackageName;// 应用包名
	private PackageInfo mPackageInfo;// 应用包信息
	private String mLauncherActivity;// 应用程序入口

	public Plugin(Context context, String pluginPath) {
		mContext = context;
		mPluginPath = pluginPath;
		CharSequence charSequence = DLUtils.getAppLabel(mContext, pluginPath);
		if (charSequence != null) {
			mPluginName = charSequence.toString();
		}
		mPackageInfo = DLUtils.getPackageInfo(mContext, pluginPath);
		mPackageName = mPackageInfo.packageName;
		if (null != mPackageInfo && mPackageInfo.activities != null
				&& mPackageInfo.activities.length > 0) {
			mLauncherActivity = mPackageInfo.activities[0].name;
		}
	}

	public String getPluginPath() {
		return mPluginPath;
	}

	public void setPluginPath(String pluginPath) {
		mPluginPath = pluginPath;
	}

	public String getPluginName() {
		return mPluginName;
	}

	public void setPluginName(String pluginName) {
		mPluginName = pluginName;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	public PackageInfo getPackageInfo() {
		return mPackageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		mPackageInfo = packageInfo;
	}

	public String getLauncherActivity() {
		return mLauncherActivity;
	}

	public void setLauncherActivity(String launcherActivity) {
		mLauncherActivity = launcherActivity;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Plugin))
			return false;
		Plugin other = (Plugin) o;
		return (this == other)
				|| (mPluginPath.equals(other.getPluginPath()));
	}
}
