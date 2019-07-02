package tech.shmy.dd_app.activity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.HistoryAdapter;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.HistoryEntity;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.list)
    public ListView listView;
    private HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        historyAdapter = new HistoryAdapter(this);
        listView.setAdapter(historyAdapter);
        List<HistoryEntity> historyEntityList = new ArrayList<>();
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyEntityList.add(new HistoryEntity());
        historyAdapter.setHistoryEntities(historyEntityList);
        historyAdapter.notifyDataSetChanged();
//        List<HistoryRepository> historyRepositoryList = HistoryDBManager.getInstance().list();
//        HistoryRepository historyRepositoryList = HistoryDBManager.getInstance().findByVid(1);
//        for (HistoryRepository historyRepository : historyRepositoryList) {
//            System.out.println("\n--------------");
//            System.out.println(historyRepository.id);
//            System.out.println(historyRepository.name);
//        }
//            System.out.println(historyRepositoryList.id);
//            System.out.println(historyRepositoryList.name);
    }
}
