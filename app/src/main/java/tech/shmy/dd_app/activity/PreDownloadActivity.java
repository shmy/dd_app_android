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
import tech.shmy.dd_app.util.DownloadManager;
import tech.shmy.dd_app.util.Util;

public class PreDownloadActivity extends BaseActivity {
    public static final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    private List<LinkEntityWithSource> linkEntityWithSources;
    private List<LinkEntity> linkEntityList = new ArrayList<>();
    private KProgressHUD kProgressHUD;
    private String name;
    @BindView(R.id.container)
    LinearLayout scrollViewChild;
    @BindView(R.id.submit)
    public Button submit;

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
            DownloadManager.getInstance().getDownloadReceiver().resumeAllTask();
            init();
            refreshSubmit();
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
                    if (this.linkEntityList.contains(linkEntity)) {

                        this.linkEntityList.remove(linkEntity);
                    } else {
                        this.linkEntityList.add(linkEntity);

                    }
                    refreshSubmit();
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

    private void refreshSubmit() {
        int count = linkEntityList.size();
        submit.setEnabled(count > 0);
        submit.setText("添加下载任务 (" + count + ")");
    }

    @OnClick(R.id.submit)
    void onSubmit() {
        kProgressHUD = KProgressHUD.create(PreDownloadActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍后")
                .setDetailsLabel("正在添加下载任务...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setCancellable(false)
                .setDimAmount(0.5f);
        for (LinkEntity linkEntity : linkEntityList) {
            String random = generateString(new Random(), SOURCES, 10);
            String filepath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + name + "_" + linkEntity.tag + "_" + random + "_.mp4";
            DownloadManager.getInstance().start(linkEntity.url, filepath);
            System.out.println(linkEntityList);
        }
        kProgressHUD.dismiss();
        linkEntityList.clear();
        refreshSubmit();
        Toasty.success(PreDownloadActivity.this, "添加任务成功", Toasty.LENGTH_LONG).show();
    }
    private String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
}
