package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.HistoryAdapter;
import tech.shmy.dd_app.database.HistoryDBManager;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.HistoryEntity;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.list)
    public ListView listView;
    @BindView(R.id.container)
    public SmartRefreshLayout smartRefreshLayout;
    private HistoryAdapter historyAdapter;
    private int page = 1;
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        historyAdapter = new HistoryAdapter(this);
        listView.setAdapter(historyAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            HistoryEntity historyEntity = historyAdapter.getItem(i);
            Intent intent = new Intent(HistoryActivity.this, VideoActivity.class);
            intent.putExtra("id", historyEntity.vid);
            intent.putExtra("pic", historyEntity.pic);
            pushActivity(intent);

        });
        smartRefreshLayout.setOnRefreshListener(this::handleRefresh);
        smartRefreshLayout.setOnLoadMoreListener(this::handleLoadMore);
        smartRefreshLayout.autoRefresh();
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
        List<HistoryEntity> historyEntityList = HistoryDBManager.getInstance().pagination(page, pageSize);
        if (historyEntityList.size() == 0) {
            refreshLayout.finishRefreshWithNoMoreData();
            refreshLayout.finishLoadMoreWithNoMoreData();
            return;
        }
        if (page == 1) {
            historyAdapter.setHistoryEntities(historyEntityList);
        } else {
            historyAdapter.addHistoryEntities(historyEntityList);
        }
        historyAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh(true);
        refreshLayout.finishLoadMore(true);
    }
}
