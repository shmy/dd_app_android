package tech.shmy.dd_app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.tendcloud.tenddata.TCAgent;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.base.UpdateChecker;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.base.UpdateStrategy;
import org.lzh.framework.updatepluginlib.model.Update;

import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.database.HistoryDBManager;
import tech.shmy.dd_app.entity.UpdateEntity;


public class DDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        UpdateConfig.getConfig()
                .setUrl("https://coding.net/u/914111374/p/dd_update/git/raw/master/update_android.json")// 配置检查更新的API接口
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        UpdateEntity updateEntity = new Gson().fromJson(response, UpdateEntity.class);
                        Update info = new Update();
                        info.setVersionName(updateEntity.version_name);
                        info.setVersionCode(updateEntity.version_code);
                        info.setUpdateUrl(updateEntity.download_url);
                        info.setUpdateContent(joinString(updateEntity.release_notes, "\n"));
                        info.setMd5(updateEntity.md5);
                        info.setIgnore(updateEntity.ignore);
                        info.setForced(updateEntity.forced);
                        return info;
                    }
                })
                .setUpdateChecker(new UpdateChecker() {
                    @Override
                    public boolean check(Update update) throws Exception {
                        return update.getVersionCode() > getVersionCode(getApplicationContext());
                    }
                })
                .setUpdateStrategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        return true;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        return true;
                    }
                })
                .setCheckCallback(new CheckCallback() {
                    @Override
                    public void onCheckStart() {
// 此方法的回调所处线程异于其他回调。其他回调所处线程为UI线程。
                        // 此方法所处线程为你启动更新任务是所在线程
                    }

                    @Override
                    public void hasUpdate(Update update) {
// 检查到有更新时通知到此
                    }

                    @Override
                    public void noUpdate() {
// 检查到无更新时通知到此
                    }

                    @Override
                    public void onCheckError(Throwable t) {
// 当更新检查错误的时候通知到此。
                        Toasty.error(getApplicationContext(), "检查更新失败", Toasty.LENGTH_LONG).show();
                    }

                    @Override
                    public void onUserCancel() {
// 用户点击取消更新的时候通知到此
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
// 检查到有更新时通知到此但是设置了忽略
                    }
                });
        TCAgent.LOG_ON = false;
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true);
        TCAgent.setAntiCheatingEnabled(this, true);
        HistoryDBManager.init(this);
    }


    private String joinString(String[] s, String e) {
        StringBuilder ret = new StringBuilder();
        for (String item : s) {
            ret.append("· ");
            ret.append(item);
            ret.append(e);
        }
        return ret.toString();
    }

    private int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = -1;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
