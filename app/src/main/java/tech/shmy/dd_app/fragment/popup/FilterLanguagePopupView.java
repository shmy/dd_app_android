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
public class FilterLanguagePopupView extends PartShadowPopupView implements View.OnClickListener {
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

    public FilterLanguagePopupView(Context context) {
        super(context);
        fields.add(new Field("不限语言", "0"));
        fields.add(new Field("汉语", "国语"));
        fields.add(new Field("粤语", "粤语"));
        fields.add(new Field("英语", "英语"));
        fields.add(new Field("德语", "德语"));
        fields.add(new Field("法语", "法语"));
        fields.add(new Field("日语", "日语"));
        fields.add(new Field("韩语", "韩语"));
        fields.add(new Field("俄语", "俄语"));
        fields.add(new Field("西班牙语", "西班牙语"));
        fields.add(new Field("意大利语", "意大利语"));
        fields.add(new Field("印度语", "印度语"));
        fields.add(new Field("印度尼西亚语", "印度尼西亚语"));
        fields.add(new Field("印地语", "印地语"));
        fields.add(new Field("泰语", "泰语"));
        fields.add(new Field("越南语", "越南语"));
        fields.add(new Field("马来西亚语", "马来西亚语"));
        fields.add(new Field("其他", "其他"));
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