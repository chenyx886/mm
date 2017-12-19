package com.chenyx.libs.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import java.util.List;

/**
 * @包名:com.gyjw.utils
 * @创建者: 陈永祥
 * @创建时间: 2016年7月19日
 * @描述: PackageUtils.java 手机系统包工具
 */
public class PackageUtils {

	/**
	 * 检查手机是否安装了某个应用
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkPackageIsInstall(Context context, String packageName) {

		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			String packageNameTemp = packageInfo.packageName;
			if (TextUtils.equals(packageNameTemp, packageName)) {
				return true;
			}
		}
		return false;
	}
}
