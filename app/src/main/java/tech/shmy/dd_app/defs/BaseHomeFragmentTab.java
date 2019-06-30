package tech.shmy.dd_app.defs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;
import java.util.Objects;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.activity.VideoActivity;
import tech.shmy.dd_app.adapter.VideoAdapter;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.util.HttpClient;


public class BaseHomeFragmentTab extends BaseFragment {
    private VideoAdapter videoAdapter;
    public SmartRefreshLayout smartRefreshLayout;
    public GridView gridView;
    public TextView title;
    public String getTitle() { return ""; };
    public int getCateGoryId() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout = view.findViewById(R.id.refresh);
        gridView = view.findViewById(R.id.grid_view);
        title = view.findViewById(R.id.title);
        videoAdapter = new VideoAdapter(getContext());
        gridView.setAdapter(videoAdapter);

        gridView.setOnItemClickListener(this::handleItemClick);
        smartRefreshLayout.setOnRefreshListener(this::handleRefresh);
        smartRefreshLayout.autoRefresh();
        title.setText("最近热门的" + getTitle());
    }

    private void handleItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), VideoActivity.class);
        intent.putExtra("id", videoAdapter.getItem(i).id);
        pushActivity(intent);
    }

    private void handleRefresh(RefreshLayout refreshLayout) {
        handleFetch(refreshLayout);
    }

    private void handleFetch(RefreshLayout refreshLayout) {
        new Thread(() -> {
            AfterResponse<List<VideoEntity>> afterResponse = HttpClient.getHotVideoList(getCateGoryId());
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                if (afterResponse.error != null) {
                    refreshLayout.finishRefresh(false);
                    refreshLayout.finishLoadMore(false);
                    return;
                }
                videoAdapter.setVideoEntities(afterResponse.data);
                videoAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(true);
                refreshLayout.finishLoadMore(true);
            });

        }).start();
    }


}
