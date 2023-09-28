package com.pandacorp.knowui.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * A compatibility wrapper around Context to get versionName from packageInfo on the new and the old api
 * @return A PackageInfo object containing information about the specified package.
 */
fun Context.getAppVersion(): String {
    val flags = 0
    val packageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, flags)
        }
    return packageInfo.versionName
}