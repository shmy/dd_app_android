package tech.shmy.dd_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.entity.VideoEntity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class VideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoEntity> videoEntities;

    public VideoAdapter(Context context) {
        this.context = context;
        this.videoEntities = new ArrayList<>();
    }

    public void setVideoEntities(List<VideoEntity> videoEntities) {
        this.videoEntities = videoEntities;
    }

    public void clearVideoEntities() {
        this.videoEntities.clear();
    }

    public void appendVideoEntities(List<VideoEntity> videoEntities) {
        this.videoEntities.addAll(videoEntities);
    }

    @Override
    public int getCount() {
        return videoEntities.size();
    }

    @Override
    public VideoEntity getItem(int i) {
        return this.videoEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.videoEntities.get(i).id;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.video_item, null);
        ImageView imageView = layout.findViewById(R.id.pic);
        TextView textView = layout.findViewById(R.id.name);
        TextView actorTextView = layout.findViewById(R.id.actor);
        VideoEntity videoEntity = videoEntities.get(i);
        Glide.with(context)
                .load(videoEntity.pic)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .transition(withCrossFade())
                .into(imageView);
        textView.setText(videoEntity.name);
        actorTextView.setText(videoEntity.actor);

        return layout;
    }

}
