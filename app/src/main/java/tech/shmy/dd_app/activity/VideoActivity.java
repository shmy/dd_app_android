package tech.shmy.dd_app.activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.utils.AnimUtils;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.component.MyButton;
import tech.shmy.dd_app.database.HistoryDBManager;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.HistoryEntity;
import tech.shmy.dd_app.entity.LinkEntity;
import tech.shmy.dd_app.entity.LinkEntityWithSource;
import tech.shmy.dd_app.entity.ResourceEntity;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.event.UserLoggedEvent;
import tech.shmy.dd_app.service.VideoService;
import tech.shmy.dd_app.util.HttpClient;
import tech.shmy.dd_app.util.Util;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoActivity extends BaseActivity {
    private ExoUserPlayer videoPlayer;
    private List<LinkEntityWithSource> linkEntityWithSources = new ArrayList<>();

    public SmartRefreshLayout smartRefreshLayout;
    public VideoPlayerView videoPlayerView;
    //    public LinearLayout scrollViewChild;
    public ImageView videoAudioImg;
    public ImageView videoBrightnessImg;
    private VideoEntity videoEntity;
    /***显示音频和亮度***/
    public ProgressBar videoAudioPro;
    public ProgressBar videoBrightnessPro;
    private long id;
    private String pic;
    private List<MyButton> myButtons = new ArrayList<>();
    private String playUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        EventBus.getDefault().register(this);
        initVideo(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (videoPlayer != null) {
            videoPlayer.releasePlayers();
        }
        initVideo(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!enterPIP()) {
            videoPlayer.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        videoPlayer.onConfigurationChanged(newConfig);//横竖屏切换
    }

    @Override
    public void onBackPressed() {
        if (videoPlayer.onBackPressed()) {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    protected void onDestroy() {
        videoPlayer.onDestroy();
        videoPlayer = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initVideo(Intent intent) {
        if (intent != null) {
            this.id = intent.getLongExtra("id", -1);
            String pic = intent.getStringExtra("pic");
            if (pic != null) {
                this.pic = pic;
            }
        }
        smartRefreshLayout = findViewById(R.id.refresh);
        smartRefreshLayout.setOnRefreshListener(this::handleFetch);
        smartRefreshLayout.autoRefresh();
        videoPlayerView = findViewById(R.id.video_player);
        videoBrightnessImg = findViewById(R.id.exo_video_brightness_img);
        videoBrightnessPro = findViewById(R.id.exo_video_brightness_pro);
        videoAudioImg = findViewById(R.id.exo_video_audio_img);
        videoAudioPro = findViewById(R.id.exo_video_audio_pro);
        VideoPlayerManager.Builder builder = new VideoPlayerManager
                .Builder(VideoPlayerManager.TYPE_PLAY_GESTURE, videoPlayerView);
        builder.setOnGestureBrightnessListener((mMax, currIndex) -> {
            //显示你的布局
            videoPlayerView.getGestureBrightnessLayout().setVisibility(View.VISIBLE);
            //为你布局显示内容自定义内容
            videoBrightnessPro.setMax(mMax);
            videoBrightnessImg.setImageResource(chuangyuan.ycj.videolibrary.R.drawable.ic_brightness_6_white_48px);
            videoBrightnessPro.setProgress(currIndex);
        });
        builder.setOnGestureVolumeListener((mMax, currIndex) -> {
            //显示你的布局
            videoPlayerView.getGestureAudioLayout().setVisibility(View.VISIBLE);
            //为你布局显示内容自定义内容
            videoAudioPro.setMax(mMax);
            videoAudioPro.setProgress(currIndex);
            videoAudioImg.setImageResource(currIndex == 0 ? R.drawable.ic_volume_off_white_48px : R.drawable.ic_volume_up_white_48px);
        });
        builder.addUpdateProgressListener(new AnimUtils.UpdateProgressListener() {
            @Override
            public void updateProgress(long position, long bufferedPosition, long duration) {
                if (videoPlayer != null && videoEntity != null) {
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.vid = id;
                    historyEntity.name = videoEntity.name;
                    historyEntity.pic = videoEntity.pic;
                    historyEntity.url = playUrl;
                    historyEntity.position = videoPlayer.getCurrentPosition();
                    historyEntity.duration = videoPlayer.getDuration();
                    HistoryDBManager.getInstance().upsertByVid(historyEntity);
                }
            }
        });
        builder.addVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart(long currPosition) {
            }

            @Override
            public void onLoadingChanged() {
            }

            @Override
            public void onPlayerError(@Nullable ExoPlaybackException e) {
                if (e.type == ExoPlaybackException.TYPE_SOURCE && playUrl.startsWith("https")) {
                    playUrl = playUrl.replaceAll("^https", "http");
                    playWithUrl(playUrl);
                    Toasty.info(VideoActivity.this, "https 线路可能失效, 尝试切换到 http 线路", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void isPlaying(boolean playWhenReady) {

            }
        });
        videoPlayer = builder.create();// 重写自定义手势监听事件
//重写自定义手势监听事件，
        if (pic != null) {
            ImageView previewImage = videoPlayerView.getPreviewImage();
            previewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(this)
                    .load(pic)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .transition(withCrossFade())
                    .into(previewImage);
        }
        videoPlayer.hideControllerView();
    }

    private void playWithUrl(String url) {
//        Alerter.create(VideoActivity.this)
//                .setTitle("Alert Title")
//                .setText("Alert text...")
//                .show();
//
//         先 reset
        if (videoPlayer != null) {
            if (videoPlayer.getPlayer() != null) {

                videoPlayer.getPlayer().stop(true);
            }
            videoPlayer.setPosition(0);
            videoPlayer.setPlayUri(url);
            videoPlayer.showControllerView();
            videoPlayer.startPlayer();
            playUrl = url;
        }
    }

    private void handleFetch(RefreshLayout refreshLayout) {
        new Thread(() -> {
            AfterResponse<VideoEntity> afterResponse = HttpClient.getVideoDetail(id);
            if (afterResponse.error != null) {
                VideoActivity.this.runOnUiThread(() -> {
                    refreshLayout.finishRefresh(false);
                });
                return;
            }
            this.videoEntity = afterResponse.data;
            transformResources(videoEntity.resources);
            VideoActivity.this.runOnUiThread(refreshLayout::finishRefresh);

        }).start();
    }

    private String appendHttp(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        return "http" + url;
    }

    private void transformResources(List<ResourceEntity> resourceEntities) {
        linkEntityWithSources.clear();
        for (ResourceEntity resourceEntity : resourceEntities) {
            List<LinkEntity> linkEntities = new ArrayList<>();
            String[] items = resourceEntity.links.split("#");
            for (String item : items) {
                String[] party = item.split("\\$");
                if (party.length < 2) {
                    party = item.split("http");
                }
                linkEntities.add(new LinkEntity(party[0], appendHttp(party[1])));
            }
            Collections.reverse(linkEntities);
            LinkEntityWithSource linkEntityWithSource = new LinkEntityWithSource(linkEntities, resourceEntity.source);
            linkEntityWithSources.add(linkEntityWithSource);

        }
        VideoActivity.this.runOnUiThread(this::run);
    }

    private void run() {
        ScrollView scrollViewContainer = findViewById(R.id.container);
        scrollViewContainer.removeAllViews();
        myButtons.clear();

        @SuppressLint("ViewHolder") LinearLayout scrollViewChild = (LinearLayout) View.inflate(this, R.layout.video_detail, null);
        TextView nameText = scrollViewChild.findViewById(R.id.name);
        ImageView tvBtn = scrollViewChild.findViewById(R.id.tv);
//        ImageView downBtn = scrollViewChild.findViewById(R.id.download);
        nameText.setText(videoEntity.name);
        tvBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ThrowingScreenActivity.class);
            intent.putExtra("linkEntityWithSources", new Gson().toJson(linkEntityWithSources));
            pushActivity(intent);
        });
//        downBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, PreDownloadActivity.class);
//            intent.putExtra("linkEntityWithSources", new Gson().toJson(linkEntityWithSources));
//            intent.putExtra("name", videoEntity.name);
//            pushActivity(intent);
//        });
        for (LinkEntityWithSource linkEntityWithSource : linkEntityWithSources) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            horizontalScrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setPadding(10, 0, 10, 0);
            for (LinkEntity linkEntity : linkEntityWithSource.links) {
                MyButton myButton = new MyButton(this);
                myButton.setUrl(linkEntity.url);
                myButton.setOnClickListener(view -> {
                    onButtonItemClick(linkEntity.url);
                });
                myButton.setText(linkEntity.tag);
                myButtons.add(myButton);
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
//        TextView nameTextView = getTextView("片名: " + videoEntity.name);
        TextView desTextView = getTextView("简介: " + videoEntity.des);
        TextView directorTextView = getTextView("导演: " + videoEntity.director);
        TextView actorTextView = getTextView("演员: " + videoEntity.actor);

//        scrollViewChild.addView(nameTextView);
        scrollViewChild.addView(directorTextView);
        scrollViewChild.addView(actorTextView);
        scrollViewChild.addView(desTextView);
        scrollViewContainer.addView(scrollViewChild);
        myButtons.get(0).performClick();
    }

    private void onButtonItemClick(String url) {
        playWithUrl(url);
        for (MyButton button : myButtons) {
            if (button.getUrl().equals(url)) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }

    }

    private TextView getTextView(String str) {
        TextView textView = new TextView(this);
        Util.setMargins(textView, 20, 10, 20, 10);
        textView.setText(str);
        return textView;
    }

    private boolean enterPIP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams params = new PictureInPictureParams.Builder().build();
            enterPictureInPictureMode(params);
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLoggedEvent(UserLoggedEvent event) {
        smartRefreshLayout.autoRefresh();
    }

}
