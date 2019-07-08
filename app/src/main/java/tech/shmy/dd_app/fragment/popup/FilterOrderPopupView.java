package tech.shmy.dd_app.fragment.popup;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allen.library.SuperTextView;
import com.lxj.xpopup.impl.PartShadowPopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;

/**
 * Description:
 * Create by dance, at 2018/12/21
 */
public class FilterOrderPopupView extends PartShadowPopupView implements View.OnClickListener {
    public class Field {
        public String label;
        public String value;

        public Field(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }

    @BindView(R.id.container)
    public LinearLayout container;

    private List<Field> fields = new ArrayList<>();
    private List<SuperTextView> items = new ArrayList<>();
    private int currentIndex = 0;

    public FilterOrderPopupView(Context context) {
        super(context);
        fields.add(new Field("最新收录", "1"));
        fields.add(new Field("最新上映", "2"));
        fields.add(new Field("最多播放", "3"));
        for (int i = 0; i < fields.size(); i++) {
            SuperTextView textView = new SuperTextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
            textView.setLeftString(fields.get(i).label);
            textView.setOnClickListener(this);
            if (i == currentIndex) {
                setSelected(textView);
            } else {
                unSetSelected(textView);
            }
            items.add(textView);
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.filter_popup;
    }

    public Field getField() {
        return fields.get(currentIndex);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this);
        for (SuperTextView item : items) {
            container.addView(item);
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    public void onClick(View v) {
        for (SuperTextView item : items) {
            unSetSelected(item);
        }
        currentIndex = items.indexOf(v);
        setSelected(items.get(currentIndex));
        dismiss();
    }
    private void setSelected(SuperTextView textView) {
        textView.setRightIcon(R.drawable.ic_baseline_done_24px);
        textView.setLeftTextColor(Color.parseColor("#008877"));
    }
    private void unSetSelected(SuperTextView textView) {
        textView.setRightIcon(null);
        textView.setLeftTextColor(Color.parseColor("#686868"));
    }

}