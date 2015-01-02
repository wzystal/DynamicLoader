package com.wzystal.dynamicloader;

import com.ryg.utils.DLUtils;

import android.content.Context;
import android.content.pm.PackageInfo;

public class Plugin {
	private Context mContext;
	private String pluginPath;// 存储路径
	private String pluginName;// 应用名称
	private PackageInfo packageInfo;// 应用包信息
	private String launcherActivityName;// 应用程序入口

	public Plugin(Context context, String pluginPath) {
		this.mContext = context;
		this.pluginPath = pluginPath;
		CharSequence charSequence = DLUtils.getAppLabel(mContext, pluginPath);
		if (charSequence != null) {
			this.pluginName = charSequence.toString();
		}
		this.packageInfo = DLUtils.getPackageInfo(mContext, pluginPath);
		if (null != this.packageInfo && this.packageInfo.activities != null
				&& this.packageInfo.activities.length > 0) {
			this.setLauncherActivityName(this.packageInfo.activities[0].name);
		}
	}

	public String getPluginPath() {
		return pluginPath;
	}

	public void setPluginPath(String pluginPath) {
		this.pluginPath = pluginPath;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	public String getLauncherActivityName() {
		return launcherActivityName;
	}

	public void setLauncherActivityName(String launcherActivityName) {
		this.launcherActivityName = launcherActivityName;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Plugin))
			return false;
		Plugin other = (Plugin) o;
		return (this == other)
				|| (this.pluginPath.equals(other.getPluginPath()));
	}
}
