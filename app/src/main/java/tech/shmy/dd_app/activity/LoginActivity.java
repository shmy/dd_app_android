package tech.shmy.dd_app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.event.UserLoggedEvent;
import tech.shmy.dd_app.util.HttpClient;
import tech.shmy.dd_app.util.Util;

public class LoginActivity extends BaseActivity {
    private Button submit;
    private Button registered;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = findViewById(R.id.submit);
        registered = findViewById(R.id.registered);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            new Thread(() -> {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                AfterResponse<String> afterResponse = HttpClient.userLogin(username, password);
                LoginActivity.this.runOnUiThread(() -> {
                    submit.setEnabled(true);
                    if (afterResponse.error != null) {
                        return;
                    }
                    Util.mmkv.encode("token", afterResponse.data);
                    EventBus.getDefault().post(new UserLoggedEvent());
                    Toasty.success(this, "登录成功").show();
                    finish();
                });
            }).start();

        });
        registered.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisteredActivity.class);
            pushActivity(intent);
            finish();
        });
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                if (value.length() < 6) {
                    usernameEditText.setError("用户名至少输入 6 位");
                    submit.setEnabled(false);
                    return;
                }
                checkError();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                if (value.length() < 6) {
                    passwordEditText.setError("密码至少输入 6 位");
                    submit.setEnabled(false);
                    return;
                }
                checkError();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkError() {
        if (usernameEditText.getError() == null &&
                passwordEditText.getError() == null &&
                usernameEditText.getText() != null &&
                passwordEditText.getText() != null
        ) {
           submit.setEnabled(true);
        }
    }
}
