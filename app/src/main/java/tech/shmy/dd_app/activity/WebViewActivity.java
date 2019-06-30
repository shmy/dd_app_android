package tech.shmy.dd_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }
}
