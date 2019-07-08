package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Query;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.VideoAdapter;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.defs.FilterListActivity;
import tech.shmy.dd_app.fragment.popup.FilterLanguagePopupView;
import tech.shmy.dd_app.fragment.popup.FilterOrderPopupView;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.fragment.popup.FilterYearPopupView;
import tech.shmy.dd_app.util.HttpClient;

public class VideoListActivity extends FilterListActivity {
    private int id;
    private String name;
    private int page = 1;
    private VideoAdapter videoAdapter;
    @BindView(R.id.container)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.grid_view)
    public GridView gridView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        setup();
    }

    private void setup() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            this.id = intent.getIntExtra("id", 0);
            this.name = intent.getStringExtra("name");
        }
        initFilter();
        toolbar.setTitle(this.name);
        videoAdapter = new VideoAdapter(this);
        gridView.setAdapter(videoAdapter);
        gridView.setOnItemClickListener(this::handleItemClick);
        smartRefreshLayout.setOnRefreshListener(this::handleRefresh);
        smartRefreshLayout.setOnLoadMoreListener(this::handleLoadMore);
        checkOfRefresh();
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
    @Override
    public void checkOfRefresh() {
        page = 1;
        String thisFields = getFieldsString();
        if (!thisFields.equals(lastFields)) {
            smartRefreshLayout.autoRefresh();
        }
    }

    private String getFieldsString() {
        return id + page + orderField + languageField + yearField;
    }

    private void handleFetch(RefreshLayout refreshLayout) {
        new Thread(() -> {
            lastFields = getFieldsString();
            AfterResponse<List<VideoEntity>> afterResponse = HttpClient.getVideoList(id, page, orderField, languageField, yearField);
            VideoListActivity.this.runOnUiThread(() -> {
                if (afterResponse.error != null) {
                    page--;
                    refreshLayout.finishRefresh(false);
                    refreshLayout.finishLoadMore(false);
                    return;
                }
                if (afterResponse.data.size() == 0) {
                    if (page == 1) {
                        videoAdapter.clearVideoEntities();
                    }
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
