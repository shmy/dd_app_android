package tech.shmy.dd_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.shmy.dd_app.entity.MenuEntity;
import tech.shmy.dd_app.entity.VideoEntity;


public class MenuAdapter extends BaseAdapter {
    private Context context;



    private List<MenuEntity> menuEntities;
    private int id;

    public MenuAdapter(Context context, List<MenuEntity> menuEntities) {
        this.context = context;
        this.menuEntities = menuEntities;
    }
    public MenuAdapter(Context context) {
        this.context = context;
    }
    public void setMenuEntities(List<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }
    public void setId(int id) {
        this.id = id;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MenuEntity menuEntity = menuEntities.get(i);
        LinearLayout relativeLayout = getLinearLayout();
        TextView textView = getTextView();
        textView.setText(menuEntity.name);
        if (menuEntity.id == id) {
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            textView.setTextColor(Color.parseColor("#ff0000"));
        }
        relativeLayout.addView(textView);
        return relativeLayout;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout relativeLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        relativeLayout.setLayoutParams(layoutParams);
        return relativeLayout;
    }
    private TextView getTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        return textView;
    }

}
