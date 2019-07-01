package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.defs.BaseActivity;
import wendu.dsbridge.DWebView;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.webview)
    public DWebView dWebView;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
         Intent intent =  getIntent();
         if (intent != null) {
             url = intent.getStringExtra("url");
         }
        init();
    }
    private void init() {
        DWebView.setWebContentsDebuggingEnabled(false);
        dWebView.getSettings().setJavaScriptEnabled(true);
        dWebView.getSettings().setSavePassword(true);
        dWebView.getSettings().setSaveFormData(true);
        dWebView.getSettings().setSupportZoom(true);
        dWebView.setJavascriptCloseWindowListener(() -> false);
//        dWebView.addJavascriptObject(new JsApi(methodChannel), "flutter");
        dWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        dWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                toolbar.setTitle("加载中...");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        dWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (dWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            dWebView.goBack(); // goBack()表示返回dWebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
