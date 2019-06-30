package tech.shmy.dd_app.fragment.home_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.activity.VideoActivity;
import tech.shmy.dd_app.adapter.VideoAdapter;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseFragment;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.util.HttpClient;

public class IndexFragmentTab extends BaseFragment {
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.grid_view)
    GridView gridView;
//    @BindView(R.id.convenientBanner)
//    public ConvenientBanner convenientBanner;
    private VideoAdapter videoAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_index, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoAdapter = new VideoAdapter(getContext());
        gridView.setAdapter(videoAdapter);
        gridView.setOnItemClickListener(this::handleItemClick);
        smartRefreshLayout.setOnRefreshListener(this::handleRefresh);
        smartRefreshLayout.autoRefresh();

//        convenientBanner.setPages(new CBViewHolderCreator() {
//            @Override
//            public Holder createHolder(View itemView) {
//                return null;
//            }
//
//            @Override
//            public int getLayoutId() {
//                return 0;
//            }
//        });

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
            AfterResponse<List<VideoEntity>> afterResponse = HttpClient.getRandList();
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
