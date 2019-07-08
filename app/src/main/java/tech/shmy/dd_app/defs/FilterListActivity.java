package tech.shmy.dd_app.defs;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.fragment.popup.FilterLanguagePopupView;
import tech.shmy.dd_app.fragment.popup.FilterOrderPopupView;
import tech.shmy.dd_app.fragment.popup.FilterYearPopupView;

interface FilterListInterface {
    void checkOfRefresh();
}
public class FilterListActivity extends BaseActivity implements FilterListInterface {
    public TextView orderTextView;
    public TextView languageTextView;
    public TextView yearTextView;
    public FilterOrderPopupView filterOrderPopupView;
    public FilterLanguagePopupView filterLanguagePopupView;
    public FilterYearPopupView filterYearPopupView;
    public Drawable upDrawable;
    public Drawable downDrawable;
    public String orderField;
    public String languageField;
    public String yearField;
    public String lastFields = "";

    public void initFilter() {
        orderTextView = findViewById(R.id.order);
        languageTextView = findViewById(R.id.language);
        yearTextView = findViewById(R.id.year);
        upDrawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24px);
        downDrawable = getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24px);
        upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());
        downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());
        filterOrderPopupView = (FilterOrderPopupView) new XPopup.Builder(this)
                .atView(orderTextView)
                .autoOpenSoftInput(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        orderTextView.setCompoundDrawables(null, null, upDrawable, null);
                    }

                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        orderTextView.setCompoundDrawables(null, null, downDrawable, null);
                        setOrder();
                        checkOfRefresh();
                    }
                })
                .asCustom(new FilterOrderPopupView(this));
        filterLanguagePopupView = (FilterLanguagePopupView) new XPopup.Builder(this)
                .atView(orderTextView)
                .autoOpenSoftInput(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        languageTextView.setCompoundDrawables(null, null, upDrawable, null);

                    }

                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        languageTextView.setCompoundDrawables(null, null, downDrawable, null);
                        setLanguage();
                        checkOfRefresh();
                    }
                })
                .asCustom(new FilterLanguagePopupView(this));
        filterYearPopupView = (FilterYearPopupView) new XPopup.Builder(this)
                .atView(orderTextView)
                .autoOpenSoftInput(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        yearTextView.setCompoundDrawables(null, null, upDrawable, null);

                    }

                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        yearTextView.setCompoundDrawables(null, null, downDrawable, null);
                        setYear();
                        checkOfRefresh();
                    }
                })
                .asCustom(new FilterYearPopupView(this));

        orderTextView.setOnClickListener(view -> filterOrderPopupView.show());
        languageTextView.setOnClickListener(view -> filterLanguagePopupView.show());
        yearTextView.setOnClickListener(view -> filterYearPopupView.show());
        setOrder();
        setLanguage();
        setYear();
    }

    private void setOrder() {
        FilterOrderPopupView.Field field = filterOrderPopupView.getField();
        orderTextView.setText(field.label);
        orderField = field.value;
    }

    private void setLanguage() {
        FilterLanguagePopupView.Field field = filterLanguagePopupView.getField();
        languageTextView.setText(field.label);
        languageField = field.value;
    }

    private void setYear() {
        FilterYearPopupView.Field field = filterYearPopupView.getField();
        yearTextView.setText(field.label);
        yearField = field.value;
    }

    @Override
    public void checkOfRefresh() {

    }
}
