package tech.shmy.dd_app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.util.Util;

import static tech.shmy.dd_app.defs.Env.WEBVIEW_BASE_URL;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.version)
    public TextView versionTextView;
    @BindView(R.id.clear_cache)
    public SuperTextView clearCacheView;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        kProgressHUD = KProgressHUD.create(AboutActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍后")
                .setDetailsLabel("正在检查更新...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setCancellable(false)
                .setDimAmount(0.5f);
        versionTextView.setText(getVersionNameAndCode(this));
        refreshCacheSize();
    }

    private String getVersionNameAndCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String result = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            result = "v " + versionName + " +" + versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @OnClick(R.id.check)
    void onCheckClick() {

        UpdateBuilder.create().setCheckCallback(new CheckCallback() {
            @Override
            public void onCheckStart() {
                kProgressHUD.setDetailsLabel("正在检查更新...").show();
            }

            @Override
            public void hasUpdate(Update update) {
                kProgressHUD.dismiss();
            }

            @Override
            public void noUpdate() {
                kProgressHUD.dismiss();
                Toasty.info(AboutActivity.this, "你使用的已是最新版本", Toasty.LENGTH_LONG).show();
            }

            @Override
            public void onCheckError(Throwable t) {
                kProgressHUD.dismiss();
                Toasty.warning(AboutActivity.this, "检查更新错误, 请稍后再试", Toasty.LENGTH_LONG).show();

            }

            @Override
            public void onUserCancel() {
            }

            @Override
            public void onCheckIgnore(Update update) {
            }
        }).check();
    }

    @OnClick(R.id.group)
    void onGruopClick() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", WEBVIEW_BASE_URL + "/join_group");
        pushActivity(intent);
    }

    @OnClick(R.id.help_center)
    void onHelpCenterClick() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", WEBVIEW_BASE_URL + "/help_center");
        pushActivity(intent);
    }

    @OnClick(R.id.feedback)
    void onFeedBackClick() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", WEBVIEW_BASE_URL + "/feedback");
        pushActivity(intent);
    }

    @OnClick(R.id.complaint)
    void onComplaintClick() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", WEBVIEW_BASE_URL + "/complaint");
        pushActivity(intent);
    }

    @OnClick(R.id.clear_cache)
    void onClearCache() {
        kProgressHUD.setDetailsLabel("正在清理缓存...").show();
        String[] keys = Util.mmkv.allKeys();
        for (String key : keys) {
            if (key.equals("token")) {
                continue;
            }
            Util.mmkv.remove(key);
        }
        refreshCacheSize();
        kProgressHUD.dismiss();
    }

    private void refreshCacheSize() {
        String[] keys = Util.mmkv.allKeys();
        long size = 0;
        for (String key : keys) {
            if (key.equals("token")) {
                continue;
            }
            size += Util.mmkv.getValueSize(key);
        }
        clearCacheView.setRightString(getSizeText(size));

    }
    private String getSizeText(long size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }

}
