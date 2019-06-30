package tech.shmy.dd_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.entity.LinkEntity;

public class LinkItemAdapter extends BaseAdapter {
    private Context context;
    private List<LinkEntity> linkEntities;

    public LinkItemAdapter(Context context) {
        this.context = context;
        this.linkEntities = new ArrayList<>();
    }

    public LinkItemAdapter(Context context, List<LinkEntity> linkEntities) {
        this.context = context;
        this.linkEntities = linkEntities;
    }

    @Override
    public int getCount() {
        return linkEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return linkEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setLinkEntities(List<LinkEntity> linkEntities) {
        this.linkEntities = linkEntities;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinkEntity linkEntity = linkEntities.get(i);
        Button button = new Button(context);
        button.setText(linkEntity.tag);
        return button;
    }

}
