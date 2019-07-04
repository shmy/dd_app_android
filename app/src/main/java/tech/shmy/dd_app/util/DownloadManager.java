package tech.shmy.dd_app.util;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadReceiver;
import com.arialyy.aria.core.download.DownloadTask;
import com.arialyy.aria.core.download.m3u8.IBandWidthUrlConverter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
    private static DownloadManager _instance;

    public static DownloadManager getInstance() {
        if (_instance == null) {
            synchronized (DownloadManager.class) {
                _instance = new DownloadManager();
            }
        }
        return _instance;
    }
    private DownloadManager() {
        Aria.download(this).register();
    }

    private static String convert(String bandWidthUrl) {
        return null;
    }

    public void start(String url, String filepath) {
        Aria.download(this)
                .load(url)
                .addHeader("Accept-Encoding", "gzip, deflate")
                .setFilePath(filepath)
                .asM3U8()
                .setTsUrlConvert(DownloadManager::tsUrlConvert)
                .setBandWidthUrlConverter(bandWidthUrl -> {
                    return DownloadManager.getTsUrl(url, bandWidthUrl);
                })
                .start();
    }

    public void stop(String key) {
        Aria.download(this).load(key).stop();
    }
    public DownloadReceiver getDownloadReceiver() {
        return Aria.download(this);
    }

    public void cancel(String key) {
        Aria.download(this).load(key).cancel();
    }

    public void unRegister() {
        Aria.download(this).unRegister();
    }
    private static List<String> tsUrlConvert(String m3u8Url, List<String> tsUrls) {
        List<String> newUrls = new ArrayList<>();
        for (String tsUrl : tsUrls) {
            newUrls.add(DownloadManager.getTsUrl(m3u8Url, tsUrl));
        }
        System.out.println(newUrls);

        return newUrls;
    }

    private static String getTsUrl(String m3u8Url, String tsUrl) {

        // 绝对路径
        if (tsUrl.startsWith("http")) {
            return tsUrl;
        }
        // 相对根路径
        if (tsUrl.startsWith("/")) {
            URL m3u8URL = null;
            try {
                m3u8URL = new URL(m3u8Url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "";
            }
            String m3u8Port = ":" + m3u8URL.getPort();
            if (m3u8Port.equals(":-1")) {
                m3u8Port = "";
            }
            return m3u8URL.getProtocol() + "://" + m3u8URL.getHost() + m3u8Port + tsUrl;
        }
        // 相对路径
        int index = m3u8Url.lastIndexOf("/");
        String parentUrl = m3u8Url.substring(0, index + 1);
        return parentUrl + tsUrl;

    }
    @Download.onPre
    protected void onPre(DownloadTask task) {
        System.out.println("onPre" + task.getKey());
    }

    @Download.onTaskStart
    protected void onTaskStart(DownloadTask task) {
        System.out.println("onTaskStart" + task.getKey());
    }

    @Download.onTaskResume
    protected void onTaskResume(DownloadTask task) {
        System.out.println("onTaskResume" + task.getKey());
    }

    //    @Download.onTaskPre protected void onTaskPre(DownloadTask task) {
//        System.out.println("onTaskPre" + task.getKey());
//    }
    @Download.onTaskRunning
    protected void onTaskRunning(DownloadTask task) {
        System.out.println("onTaskRunning" + task.getKey());
    }

    @Download.onTaskCancel
    protected void onTaskCancel(DownloadTask task) {
        System.out.println("onTaskCancel" + task.getKey());
    }

    @Download.onTaskStop
    protected void onTaskStop(DownloadTask task) {
        System.out.println("onTaskStop" + task.getKey());
    }

    @Download.onTaskFail
    protected void onTaskFail(DownloadTask task) {
        System.out.println("onTaskFail");
    }

    @Download.onTaskComplete
    protected void onTaskComplete(DownloadTask task) {
        System.out.println("onTaskComplete" + task.getKey());
    }
}
