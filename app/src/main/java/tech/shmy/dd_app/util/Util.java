package tech.shmy.dd_app.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.mmkv.MMKV;

public class Util {
    public static MMKV mmkv = MMKV.defaultMMKV();

    public static void setMargins(View v, int l, int t, int r, int b) {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
        if (p == null) {
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        System.out.println(p);
        p.setMargins(l, t, r, b);
        v.setLayoutParams(p);
    }
}
