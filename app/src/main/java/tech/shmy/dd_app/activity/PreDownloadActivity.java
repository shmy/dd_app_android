package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.component.MyButton;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.LinkEntity;
import tech.shmy.dd_app.entity.LinkEntityWithSource;
import tech.shmy.dd_app.util.M3u8Download;
import tech.shmy.dd_app.util.Util;

public class PreDownloadActivity extends BaseActivity {
    private final String SITE_NAME = "黑人视频";
    private List<LinkEntityWithSource> linkEntityWithSources;
    private KProgressHUD kProgressHUD;
    private String name;
    @BindView(R.id.container)
    LinearLayout scrollViewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_download);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            String linkEntityWithSourcesString = intent.getStringExtra("linkEntityWithSources");
            name = intent.getStringExtra("name");
            LinkEntityWithSource[] linkEntityWithSources = new Gson().fromJson(linkEntityWithSourcesString, LinkEntityWithSource[].class);
            this.linkEntityWithSources = Arrays.asList(linkEntityWithSources);
            init();
        }
    }

    private void init() {
        for (LinkEntityWithSource linkEntityWithSource : linkEntityWithSources) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            horizontalScrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            for (LinkEntity linkEntity : linkEntityWithSource.links) {
                MyButton myButton = new MyButton(this);
                myButton.setUrl(linkEntity.url);
                myButton.setOnClickListener(view -> {
                    doDownload(linkEntity);
                });
                myButton.setText(linkEntity.tag);
                linearLayout.addView(myButton);
            }
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            TextView textView = new TextView(this);
            textView.setText(linkEntityWithSource.source);
            textView.setTextSize(20);
            Util.setMargins(textView, 20, 20, 20, 20);
            scrollViewChild.addView(textView);
            horizontalScrollView.addView(linearLayout);
            scrollViewChild.addView(horizontalScrollView);

        }
    }


    private void doDownload(LinkEntity linkEntity) {
        kProgressHUD = KProgressHUD.create(PreDownloadActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍后")
                .setDetailsLabel("正在添加下载任务...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setCancellable(false)
                .setDimAmount(0.5f);

        String url = linkEntity.url;
        String filename = name + "-" + linkEntity.tag + "[" + SITE_NAME + "]";
        M3u8Download.download(this, filename, url);
        kProgressHUD.dismiss();
//        Toasty.success(PreDownloadActivity.this, "添加任务成功", Toasty.LENGTH_LONG).show();

    }
}
