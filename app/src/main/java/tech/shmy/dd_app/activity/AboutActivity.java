package tech.shmy.dd_app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;

import static tech.shmy.dd_app.defs.Env.WEBVIEW_BASE_URL;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.version)
    public TextView versionTextView;
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
                kProgressHUD.show();
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

}
