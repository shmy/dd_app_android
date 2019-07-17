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
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.database.HistoryDBManager;
import tech.shmy.dd_app.entity.HistoryEntity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class HistoryAdapter extends BaseAdapter {
    private Context context;


    private List<HistoryEntity> historyEntities;

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

    public void addHistoryEntities(List<HistoryEntity> historyEntities) {
        this.historyEntities.addAll(historyEntities);
    }

    @Override
    public int getCount() {
        return historyEntities.size();
    }

    @Override
    public HistoryEntity getItem(int i) {
        return historyEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HistoryEntity historyEntity = historyEntities.get(i);
        @SuppressLint("ViewHolder") LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.history_item, null);
        ImageView pic = layout.findViewById(R.id.pic);
        TextView textView = layout.findViewById(R.id.name);
        ImageView button = layout.findViewById(R.id.more);
        Glide.with(context)
                .load(historyEntity.pic)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .transition(withCrossFade())
                .circleCrop()
                .into(pic);
        textView.setText(historyEntity.name);
        button.setOnClickListener(view1 -> {
            new XPopup.Builder(context)
                    .atView(view1)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .hasShadowBg(false)
                    .asAttachList(new String[]{"删除记录"},
                            new int[]{},
//                                new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                            (position, text) -> {
                                switch (position) {
                                    case 0: {
                                        HistoryDBManager.getInstance().deleteById(historyEntity.id);
                                        historyEntities.remove(i);
                                        notifyDataSetChanged();
                                    }
                                    break;
                                    default:
                                        break;
                                }

                            })
                    .show();
        });
        return layout;
    }

}
