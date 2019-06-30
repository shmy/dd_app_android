package tech.shmy.dd_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.entity.MenuEntity;

public class MenuContainerAdapter extends BaseAdapter {
    private Context context;
    private List<MenuEntity> menuEntities;

    public MenuContainerAdapter(Context context) {
        this.context = context;
        this.menuEntities = new ArrayList<>();

    }

    public MenuContainerAdapter(Context context, List<MenuEntity> menuEntities) {
        this.context = context;
        this.menuEntities = menuEntities;
    }

    @Override
    public int getCount() {
        return menuEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return menuEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setMenuEntities(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MenuEntity menuEntity = menuEntities.get(i);
        @SuppressLint("ViewHolder") RelativeLayout layout = (RelativeLayout) View.inflate(context, R.layout.menu_container_item, null);
        TextView textView = layout.findViewById(R.id.name);
        textView.setText(menuEntity.name);

        return layout;
    }

}
