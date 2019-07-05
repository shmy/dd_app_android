package tech.shmy.dd_app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import tech.shmy.dd_app.activity.WebViewActivity;

import static tech.shmy.dd_app.defs.Env.WEBVIEW_BASE_URL;

public class M3u8Download {
    private static String PACKAGE_NAME = "ru.yourok.m3u8loader";
    private static String ACTIVITY_PATH = ".activitys.AddListActivity";

    public static void download(Context context, String name, String url) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(PACKAGE_NAME);
        ComponentName comp = new ComponentName(PACKAGE_NAME, PACKAGE_NAME + ACTIVITY_PATH);
        if (intent != null) {
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("name", name);
            intent.putExtra("download", true);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(intent);
        } else {
            installM3u8Loader(context);
        }
    }
    public static boolean isInstalled(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static void installM3u8Loader(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", WEBVIEW_BASE_URL + "/m3u8download");
        context.startActivity(intent);
    }

}
