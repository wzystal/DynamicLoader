package com.wzystal.dynamicloader.util;

import java.util.ArrayList;
import java.util.List;

import com.ryg.utils.DLUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import static com.wzystal.dynamicloader.util.Constant.*;

public class DLHelper extends DLUtils{
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if(drawable==null) return null;
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
	
	public static List<String> getInstalledPackageName(Context context) {
		List<String> packageNameList = new ArrayList<String>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : packageInfos) {
			packageNameList.add(packageInfo.packageName);
//			LogHelper.d(TAG, "已安装应用：" + packageInfo.packageName);
		}
		return packageNameList;
	}
}
