package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.widget.Toolbar;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.VideoAdapter;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.util.HttpClient;

public class VideoListActivity extends BaseActivity {
    private int id;
    private String name;
    private int page = 1;
    private VideoAdapter videoAdapter;
    public SmartRefreshLayout smartRefreshLayout;
    public GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        smartRefreshLayout = findViewById(R.id.container);
        gridView = findViewById(R.id.grid_view);
        setup();
    }

    private void setup() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            this.id = intent.getIntExtra("id", 0);
            this.name = intent.getStringExtra("name");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(this.name);
        videoAdapter = new VideoAdapter(this);
        gridView.setAdapter(videoAdapter);
        gridView.setOnItemClickListener(this::handleItemClick);
        smartRefreshLayout.setOnRefreshListener(this::handleRefresh);
        smartRefreshLayout.setOnLoadMoreListener(this::handleLoadMore);
        smartRefreshLayout.autoRefresh();
    }

    private void handleItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("id", videoAdapter.getItem(i).id);
        intent.putExtra("pic", videoAdapter.getItem(i).pic);
        pushActivity(intent);
    }

    private void handleRefresh(RefreshLayout refreshLayout) {
        page = 1;
        handleFetch(refreshLayout);
    }

    private void handleLoadMore(RefreshLayout refreshLayout) {
        page++;
        handleFetch(refreshLayout);

    }

    private void handleFetch(RefreshLayout refreshLayout) {
        new Thread(() -> {
            AfterResponse<List<VideoEntity>> afterResponse = HttpClient.getVideoList(id, page);
            VideoListActivity.this.runOnUiThread(() -> {
                if (afterResponse.error != null) {
                    page --;
                    refreshLayout.finishRefresh(false);
                    refreshLayout.finishLoadMore(false);
                    return;
                }
                if (afterResponse.data.size() == 0) {
                    refreshLayout.finishRefreshWithNoMoreData();
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    return;
                }
                if (page == 1) {
                    videoAdapter.setVideoEntities(afterResponse.data);
                } else {
                    videoAdapter.appendVideoEntities(afterResponse.data);
                }
                videoAdapter.notifyDataSetChanged();
                refreshLayout.resetNoMoreData();
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);
            });

        }).start();
    }
}
