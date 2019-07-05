package tech.shmy.dd_app.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import tech.shmy.dd_app.activity.HistoryActivity;
import tech.shmy.dd_app.activity.SearchActivity;
import tech.shmy.dd_app.activity.SearchResultActivity;
import tech.shmy.dd_app.activity.VideoActivity;
import tech.shmy.dd_app.activity.VideoListActivity;
import tech.shmy.dd_app.activity.WebViewActivity;

public class JsApi {
    public class VideoArgs {
        public long id;
        public String pic;
    }

    public class CategoryArgs {
        public int id;
        public String name;
    }

    public class DownloadArgs {
        public String url;
        public String name;
    }

    private static Context context;

    public static void init(Context context) {
        JsApi.context = context;
    }

    // 同步API 打开视频详情页
    @JavascriptInterface
    public void openVideoDetail(Object args) {
        try {
            VideoArgs videoArgs = new Gson().fromJson(args.toString(), VideoArgs.class);
            Intent intent = new Intent(JsApi.context, VideoActivity.class);
            intent.putExtra("id", videoArgs.id);
            intent.putExtra("pic", videoArgs.pic);
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 同步API 打开一个 WebView
    @JavascriptInterface
    public void openWebView(Object args) {
        try {
            Intent intent = new Intent(JsApi.context, WebViewActivity.class);
            String url = args.toString();
            intent.putExtra("url", url);
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 同步API 复制文字到剪贴板
    @JavascriptInterface
    public void copyText(Object args) {
        ClipboardManager cm = (ClipboardManager) JsApi.context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", args.toString());
        // 将ClipData内容放到系统剪贴板里。
        assert cm != null;
        cm.setPrimaryClip(mClipData);
    }

    // 同步API 打开指定分类
    @JavascriptInterface
    public void openCategory(Object args) {
        try {
            CategoryArgs categoryArgs = new Gson().fromJson(args.toString(), CategoryArgs.class);

            Intent intent = new Intent(JsApi.context, VideoListActivity.class);
            intent.putExtra("id", categoryArgs.id);
            intent.putExtra("name", categoryArgs.name);
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 同步API 打开历史记录
    @JavascriptInterface
    public void openHistory(Object args) {
        try {
            Intent intent = new Intent(JsApi.context, HistoryActivity.class);
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 同步API 打开搜索页面
    @JavascriptInterface
    public void openSearch(Object args) {
        try {
            Intent intent = new Intent(JsApi.context, SearchActivity.class);
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 同步API 打开搜索结果页面
    @JavascriptInterface
    public void openSearchResult(Object args) {
        try {
            Intent intent = new Intent(JsApi.context, SearchResultActivity.class);
            intent.putExtra("keyword", args.toString());
            JsApi.context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 同步API 调用下载
    @JavascriptInterface
    public void callDownload(Object args) {
        try {
            DownloadArgs downloadArgs = new Gson().fromJson(args.toString(), DownloadArgs.class);
            M3u8Download.download(JsApi.context, downloadArgs.name, downloadArgs.url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //异步API
//    @JavascriptInterface
//    public void testAsyn(Object msg, CompletionHandler<String> handler) {
//        handler.complete(msg+" [ asyn call]");
//    }
}
