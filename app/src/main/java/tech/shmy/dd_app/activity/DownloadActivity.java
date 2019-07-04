package tech.shmy.dd_app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;


import com.arialyy.aria.core.download.DownloadEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.DownloadAdapter;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.util.DownloadManager;

public class DownloadActivity extends BaseActivity {

    @BindView(R.id.list)
    public ListView listView;
    private DownloadAdapter downloadAdapter;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<DownloadEntity> downloadEntityList = DownloadManager
                    .getInstance()
                    .getDownloadReceiver()
                    .getTaskList(1, 20);
            if (downloadEntityList == null) {
                downloadEntityList = new ArrayList<>();
            }
            downloadAdapter.setDownloadEntities(downloadEntityList);
            downloadAdapter.notifyDataSetChanged();
            handler.postDelayed(runnable, 300);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        // 任务占用
        //"https://youku.kuyun-leshi.com/20190630/yC5XWuG8/index.m3u8" -> m3u8 重定向
        //"https://youku.kuyun-leshi.com/20190630/yC5XWuG8/index.m3u8" -> m3u8加密
//        DownloadManager.getInstance().getDownloadReceiver().removeAllTask(true);
//        DownloadManager.getInstance().start("https://meigui.qqqq-kuyun.com/20190630/10202_a759d0da/800k/hls/index.m3u8",
//                Environment.getExternalStorageDirectory().getPath() + "/Download/111.mp4");
        init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void init() {
        downloadAdapter = new DownloadAdapter(DownloadActivity.this);
        listView.setAdapter(downloadAdapter);
        handler.post(runnable);
    }
}
