package tech.shmy.dd_app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.UserEntity;
import tech.shmy.dd_app.util.HttpClient;

public class RegisteredActivity extends BaseActivity {
    private Button submit;
    private Button login;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rePasswordEditText = findViewById(R.id.re_password);
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            if (!checkError()) {
                submit.setEnabled(true);
                return;
            }
            new Thread(() -> {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String re_password = rePasswordEditText.getText().toString();

                AfterResponse<UserEntity> afterResponse = HttpClient.userRegister(username, password, re_password);
                RegisteredActivity.this.runOnUiThread(() -> {
                    submit.setEnabled(true);
                    if (afterResponse.error != null) {
                        return;
                    }
                    Toasty.success(this, "注册成功").show();
                    Intent intent = new Intent(RegisteredActivity.this, LoginActivity.class);
                    pushActivity(intent);
                    finish();
                });
            }).start();

        });
        login.setOnClickListener(view -> {
            Intent intent = new Intent(RegisteredActivity.this, LoginActivity.class);
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
                    return;
                }
                usernameEditText.setError(null);
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
                    return;
                }
                passwordEditText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rePasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                if (value.length() < 6) {
                    rePasswordEditText.setError("确认密码至少输入 6 位");
                    return;
                }
                rePasswordEditText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean checkError() {
        if (usernameEditText.getError() == null &&
                passwordEditText.getError() == null &&
                rePasswordEditText.getError() == null &&
                usernameEditText.getText() != null &&
                passwordEditText.getText() != null &&
                rePasswordEditText.getText() != null
        ) {
            return true;
        }
        return false;
    }
}
