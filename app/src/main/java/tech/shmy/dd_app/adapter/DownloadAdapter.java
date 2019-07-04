package tech.shmy.dd_app.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.inf.IEntity;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.BuildConfig;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.util.DownloadManager;


public class DownloadAdapter extends BaseAdapter {
    private Context context;

    private List<DownloadEntity> downloadEntities;

    public DownloadAdapter(Context context, List<DownloadEntity> downloadEntities) {
        this.context = context;
        this.downloadEntities = downloadEntities;
    }

    public DownloadAdapter(Context context) {
        this.context = context;
        this.downloadEntities = new ArrayList<>();

    }

    public void setDownloadEntities(List<DownloadEntity> downloadEntities) {
        this.downloadEntities = downloadEntities;
    }

    @Override
    public int getCount() {
        return downloadEntities.size();
    }

    @Override
    public DownloadEntity getItem(int i) {
        return downloadEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DownloadEntity downloadEntity = downloadEntities.get(i);
        @SuppressLint("ViewHolder") LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.download_item, null);
        TextView filename = layout.findViewById(R.id.filename);
        TextView state = layout.findViewById(R.id.state);
        Button primaryButton = layout.findViewById(R.id.primary_button);
        ProgressBar progressBar = layout.findViewById(R.id.progress);
        ImageView moreButton = layout.findViewById(R.id.more);

        filename.setText(downloadEntity.getFileName());
        state.setText(getStateText(downloadEntity.getState()));
        progressBar.setProgress(downloadEntity.getPercent());
        String primaryButtonText = getPrimaryButtonText(downloadEntity.getState());
        if (primaryButtonText.isEmpty()) {
            primaryButton.setVisibility(View.INVISIBLE);
        } else {
            primaryButton.setVisibility(View.VISIBLE);
            primaryButton.setText(primaryButtonText);
        }
        primaryButton.setOnClickListener(view1 -> {
            OnClick(downloadEntity);
        });

        moreButton.setOnClickListener(view12 -> {
            new XPopup.Builder(context)
                    .atView(view12)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .asAttachList(new String[]{"复制下载链接", "删除"},
                            new int[]{},
//                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                            (position, text) -> {
                                switch (position) {
                                    case 0:
                                        copy(downloadEntity.getUrl());
                                        break;
//                                    case 1: {
//                                        if (!downloadEntity.isComplete()) {
//                                            Toasty.warning(context, "下载完成可用").show();
//                                            return;
//                                        }
//                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                        Uri uri = Uri.parse(downloadEntity.getFilePath());
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                                        intent.setDataAndType(uri, "*/*");
//                                        context.startActivity(intent);
//                                    }
//                                    break;
                                    case 1:
                                        DownloadManager.getInstance().getDownloadReceiver().load(downloadEntity.getUrl()).cancel(true);
                                        break;
                                    default:
                                        break;
                                }

                            })
                    .show();
        });
        return layout;
    }

    private String getPrimaryButtonText(int state) {
        switch (state) {
            case IEntity.STATE_OTHER:
                return "";
            case IEntity.STATE_FAIL:
                return "重试";
            case IEntity.STATE_COMPLETE:
                return "播放";
            case IEntity.STATE_STOP:
                return "恢复";
            case IEntity.STATE_WAIT:
                return "停止";
            case IEntity.STATE_RUNNING:
                return "停止";
            case IEntity.STATE_PRE:
                return "停止";
            case IEntity.STATE_POST_PRE:
                return "停止";
            case IEntity.STATE_CANCEL:
                return "";
            default:
                return "未知";
        }
    }

    private String getStateText(int state) {
        switch (state) {
            case IEntity.STATE_OTHER:
                return "其他";
            case IEntity.STATE_FAIL:
                return "下载失败";
            case IEntity.STATE_COMPLETE:
                return "完成";
            case IEntity.STATE_STOP:
                return "已停止";
            case IEntity.STATE_WAIT:
                return "等待中";
            case IEntity.STATE_RUNNING:
                return "下载中";
            case IEntity.STATE_PRE:
                return "连接中";
            case IEntity.STATE_POST_PRE:
                return "已连接";
            case IEntity.STATE_CANCEL:
                return "已删除";
            default:
                return "未知";
        }
    }

    private void OnClick(DownloadEntity downloadEntity) {
        // currentProgress
        int state = downloadEntity.getState();
        switch (state) {
            case IEntity.STATE_WAIT:
            case IEntity.STATE_RUNNING:
            case IEntity.STATE_PRE:
            case IEntity.STATE_POST_PRE:
                DownloadManager.getInstance().getDownloadReceiver().load(downloadEntity.getUrl()).stop();
                break;
            case IEntity.STATE_FAIL:
            case IEntity.STATE_STOP:
                DownloadManager.getInstance().getDownloadReceiver().load(downloadEntity.getUrl()).reStart();
                break;
            case IEntity.STATE_COMPLETE: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String path = downloadEntity.getFilePath();//该路径可以自定义
                Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", new File(path));
                intent.setDataAndType(uri, "video/*");
                context.startActivity(intent);
            }
            break;
            default:
                break;
        }

    }
    private void copy(String text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        assert cm != null;
        cm.setPrimaryClip(mClipData);
    }

}
