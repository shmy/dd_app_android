package tech.shmy.dd_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.entity.MenuEntity;
import tech.shmy.dd_app.entity.VideoEntity;


public class SearchItemAdapter extends BaseAdapter {
    private Context context;
    private List<VideoEntity> videoEntities;

    public SearchItemAdapter(Context context) {
        this.context = context;
        this.videoEntities = new ArrayList<>();
    }

    public void setVideoEntities(List<VideoEntity> videoEntities) {
        this.videoEntities = videoEntities;
    }
    public void clearVideoEntities() {
        this.videoEntities.clear();
    }
    @Override
    public int getCount() {
        return videoEntities.size();
    }

    @Override
    public VideoEntity getItem(int i) {
        return videoEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VideoEntity videoEntity = videoEntities.get(i);
        LinearLayout relativeLayout = getLinearLayout();
        TextView textView = getTextView();
        textView.setText(videoEntity.name);
//        if (videoEntities.id == id) {
//            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//            textView.setTextColor(Color.parseColor("#ff0000"));
//        }
        relativeLayout.addView(textView);
        return relativeLayout;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout relativeLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        relativeLayout.setLayoutParams(layoutParams);
        return relativeLayout;
    }
    private TextView getTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(10, 0, 10, 0);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.START);
        return textView;
    }

}
