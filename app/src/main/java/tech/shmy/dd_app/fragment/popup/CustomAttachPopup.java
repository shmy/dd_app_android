package tech.shmy.dd_app.fragment.popup;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.HorizontalAttachPopupView;

import tech.shmy.dd_app.R;


public class CustomAttachPopup extends HorizontalAttachPopupView {
    OnContinueClickListener onContinueClickListener;
    String tip;
    public CustomAttachPopup(@NonNull Context context, OnContinueClickListener onContinueClickListener, String tip) {
        super(context);
        this.onContinueClickListener = onContinueClickListener;
        this.tip = tip;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView textView = findViewById(R.id.tips);

        textView.setText(tip);
        findViewById(R.id._continue).setOnClickListener(v -> {
            onContinueClickListener.onClick();
            dismiss();
        });
        findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
    }
}