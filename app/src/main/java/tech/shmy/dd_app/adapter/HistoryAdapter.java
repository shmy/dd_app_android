package tech.shmy.dd_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.entity.HistoryEntity;


public class HistoryAdapter extends BaseAdapter {
    private Context context;


    private List<HistoryEntity> historyEntities;
    private int id;

    public HistoryAdapter(Context context, List<HistoryEntity> historyEntities) {
        this.context = context;
        this.historyEntities = historyEntities;
    }

    public HistoryAdapter(Context context) {
        this.context = context;
        this.historyEntities = new ArrayList<>();

    }

    public void setHistoryEntities(List<HistoryEntity> historyEntities) {
        this.historyEntities = historyEntities;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return historyEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return historyEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HistoryEntity menuEntity = historyEntities.get(i);
        LinearLayout relativeLayout = getLinearLayout();
        SuperTextView textView = getTextView();

        relativeLayout.addView(textView);
        return relativeLayout;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout relativeLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);
        return relativeLayout;
    }

    private SuperTextView getTextView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SuperTextView textView = new SuperTextView(context);
        textView.setLeftString("圣火令");
        textView.setLeftBottomString("魔教教主");
//        textView.setLeftIcon(0);
        textView.setPadding(0, 20, 0, 20);
        textView.setLeftTextGravity(Gravity.CENTER_VERTICAL);
        textView.setRightIcon(R.drawable.ic_baseline_keyboard_arrow_right_24px);
        return textView;
    }

}
