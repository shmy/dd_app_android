package tech.shmy.dd_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap> deviceList = new ArrayList<>();
    private String uuid;
    public DeviceAdapter(Context context) {
        this.context = context;
    }
    public void setDeviceList(List<HashMap> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HashMap device = deviceList.get(i);
        String _uuid = (String) device.get("uuid");
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 85));
        textView.setPadding(20, 0, 20, 0);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(String.format("%s (%s)", device.get("name"), device.get("ip")));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        if (_uuid.equals(uuid)) {
            textView.setTextColor(Color.parseColor("#008877"));
        }
        return textView;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
