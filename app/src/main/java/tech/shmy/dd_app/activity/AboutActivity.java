package tech.shmy.dd_app.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.version)
    public TextView versionTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
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
            result = versionName + "+-" + versionCode;
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
                Toasty.info(AboutActivity.this, "检查更新", Toasty.LENGTH_LONG).show();
            }

            @Override
            public void hasUpdate(Update update) {

                Toasty.info(AboutActivity.this, "有更新", Toasty.LENGTH_LONG).show();
            }

            @Override
            public void noUpdate() {
                Toasty.info(AboutActivity.this, "无更新", Toasty.LENGTH_LONG).show();
            }

            @Override
            public void onCheckError(Throwable t) {
                Toasty.info(AboutActivity.this, "出现错误", Toasty.LENGTH_LONG).show();
            }

            @Override
            public void onUserCancel() {

            }

            @Override
            public void onCheckIgnore(Update update) {

            }
        }).check();
    }
}
