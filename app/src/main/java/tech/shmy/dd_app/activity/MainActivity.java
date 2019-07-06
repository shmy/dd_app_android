package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.adapter.BottomViewAdapter;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.fragment.HomeFragment;
import tech.shmy.dd_app.fragment.CategoryFragment;
import tech.shmy.dd_app.fragment.MineFragment;
import tech.shmy.dd_app.util.HttpClient;
import tech.shmy.dd_app.util.JsApi;

public class MainActivity extends BaseActivity {
    public ViewPager viewPager;
    public BottomNavigationView navView;
    private final OnNavigationItemSelectedListener navigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            navView.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        JsApi.init(this);

        HttpClient.init(this);
        setContentView(R.layout.activity_main);
        setScheme();
        checkUpdate();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CategoryFragment());
        fragments.add(new MineFragment());
        BottomViewAdapter adapter = new BottomViewAdapter(getSupportFragmentManager(), fragments);
        viewPager = findViewById(R.id.viewpager_launch);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setScheme() {
        Uri data = getIntent().getData();
        if (data != null) {
            System.out.println("---------");
            String type = data.getQueryParameter("type");
            if (type.equals("video")) {
                try {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    JsApi.VideoArgs videoArgs = new Gson().fromJson(data.getQueryParameter("data"), JsApi.VideoArgs.class);
                    intent.putExtra("id", videoArgs.id);
                    intent.putExtra("pic", videoArgs.pic);
                    pushActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void checkUpdate() {
        UpdateBuilder.create()
                .check();// 启动更新任务
    }
//    private void checkUpdate2() {
//        UpdateManager.create(MainActivity.this)
//                .setUrl("https://coding.net/u/914111374/p/dd_update/git/raw/master/update_android.json")
//                .setParser(source -> {
//                    UpdateInfo info = new UpdateInfo();
//                    UpdateEntity updateEntity = new Gson().fromJson(source, UpdateEntity.class);
//                    System.out.println(updateEntity.download_url);
//                    // 是否有新版本
//                    info.hasUpdate = true;
//                    // 是否静默下载：有新版本时不提示直接下载
//                    info.isSilent = false;
//                    // 是否强制安装：不安装无法使用app
//                    info.isForce = true;
//                    // 是否下载完成后自动安装
//                    info.isAutoInstall = true;
//                    // 是否可忽略该版本
//                    info.isIgnorable = false;
//
//                    info.versionCode = updateEntity.version_code;
//                    info.versionName = updateEntity.version_name;
//                    info.updateContent = joinString(updateEntity.release_notes, "\n");
//                    info.url = updateEntity.download_url;
//                    info.md5 = updateEntity.md5;
//                    info.size = updateEntity.size;
//
//                    return info;
//                })
//                .check();
//
//    }


}
