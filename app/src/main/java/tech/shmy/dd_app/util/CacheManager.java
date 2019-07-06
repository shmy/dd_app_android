package tech.shmy.dd_app.util;

import com.google.gson.Gson;

import java.util.Date;

import tech.shmy.dd_app.entity.VideoEntity;

public class CacheManager {
    public static class CachedVideoEntity {
        public VideoEntity data;
        public long created_at;

    }
    public static final long timeout = 600000; // 缓存十分钟

    public static VideoEntity setCacheVideo(VideoEntity videoEntity) {
        try {
            CachedVideoEntity cachedVideoEntity = new CachedVideoEntity();
            cachedVideoEntity.created_at = new Date().getTime();
            cachedVideoEntity.data = videoEntity;
            String key = videoEntity.id + "";
            Util.mmkv.encode(key, new Gson().toJson(cachedVideoEntity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoEntity;
    }

    public static VideoEntity getCacheVideo(long id) {
        try {
            String key = id + "";
            String jsonStr = Util.mmkv.decodeString(key);
            CachedVideoEntity cachedVideoEntity = new Gson().fromJson(jsonStr, CachedVideoEntity.class);
            long now = new Date().getTime();
            if (now - cachedVideoEntity.created_at < timeout) {
                return cachedVideoEntity.data;
            }
            Util.mmkv.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
