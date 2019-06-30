package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.DeviceAdapter;
import tech.shmy.dd_app.component.MyButton;
import tech.shmy.dd_app.cyberlink.engine.DLNAContainer;
import tech.shmy.dd_app.cyberlink.engine.MultiPointController;
import tech.shmy.dd_app.cyberlink.service.DLNAService;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.LinkEntity;
import tech.shmy.dd_app.entity.LinkEntityWithSource;
import tech.shmy.dd_app.util.Util;

public class ThrowingScreenActivity extends BaseActivity {
    private List<LinkEntityWithSource> linkEntityWithSources;
    private List<MyButton> myButtons = new ArrayList<>();
    private List<HashMap> deviceList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private MultiPointController multiPointController = new MultiPointController();
    private String playUrl;
    @BindView(R.id.devices)
    ListView devices;
    @BindView(R.id.container)
    LinearLayout scrollViewChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throwing_screen);
        ButterKnife.bind(this);
        startDLNAService();
        if (getIntent() != null) {
            String linkEntityWithSourcesString = getIntent().getStringExtra("linkEntityWithSources");
            LinkEntityWithSource[] linkEntityWithSources = new Gson().fromJson(linkEntityWithSourcesString, LinkEntityWithSource[].class);
            this.linkEntityWithSources = Arrays.asList(linkEntityWithSources);
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNAService();
    }

    private void init() {
        deviceAdapter = new DeviceAdapter(this);
        devices.setAdapter(deviceAdapter);
        devices.setOnItemClickListener((adapterView, view, i, l) -> {
           String uuid = (String) deviceList.get(i).get("uuid");
            deviceAdapter.setUuid(uuid);
            deviceAdapter.notifyDataSetChanged();
        });
        renderDevices();
        for (LinkEntityWithSource linkEntityWithSource : linkEntityWithSources) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            horizontalScrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            for (LinkEntity linkEntity : linkEntityWithSource.links) {
                MyButton myButton = new MyButton(this);
                myButton.setUrl(linkEntity.url);
                myButton.setOnClickListener(view -> {
                    onButtonItemClick(linkEntity.url);
                });
                myButton.setText(linkEntity.tag);
                myButtons.add(myButton);
                linearLayout.addView(myButton);
            }
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            TextView textView = new TextView(this);
            textView.setText(linkEntityWithSource.source);
            textView.setTextSize(20);
            Util.setMargins(textView, 20, 20, 20, 20);
            scrollViewChild.addView(textView);
            horizontalScrollView.addView(linearLayout);
            scrollViewChild.addView(horizontalScrollView);

        }

        myButtons.get(0).performClick();

    }

    private void onButtonItemClick(String url) {
        playUrl = url;
        for (MyButton button : myButtons) {
            if (button.getUrl().equals(url)) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }

    }

    private void startDLNAService() {

        DLNAContainer.getInstance().setDeviceChangeListener(new DLNAContainer.DeviceChangeListener() {
            @Override
            public void onDeviceChange(Device device) {
                renderDevices();
            }
        });
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        startService(intent);

    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        stopService(intent);
    }

    private Device findDevice(String uuid) {
        List<Device> devices = DLNAContainer.getInstance().getDevices();
        for (Device device1 : devices) {
            if (uuid.equalsIgnoreCase(device1.getUDN())) {
                return device1;
            }
        }
        return null;
    }
    private void refreshDevicesList() {
        deviceList.clear();
        List<Device> devices = DLNAContainer.getInstance().getDevices();
        for (Device device1 : devices) {
            HashMap hm = new HashMap();
            hm.put("name", device1.getFriendlyName());
            hm.put("ip", device1.getInterfaceAddress());
            hm.put("uuid", device1.getUDN());
            deviceList.add(hm);
        }
    }
    private void renderDevices() {
        new Thread(() -> {
            refreshDevicesList();
            ThrowingScreenActivity.this.runOnUiThread(() -> {
                deviceAdapter.setDeviceList(deviceList);
                deviceAdapter.notifyDataSetChanged();
                System.out.println(deviceList);
            });
        }).start();
//        System.out.println(deviceList);
    }
    private void handlePlay(String uuid) {
        new Thread(() -> {
            final Device device = findDevice(uuid);
            if (device != null) {
                multiPointController.play(device, playUrl);
            }
        }).start();

    }
    @OnClick(R.id.submit)
    void onSubmit() {
        String uuid = deviceAdapter.getUuid();
        if (uuid == null) {
            return;
        }
        handlePlay(uuid);
    }
}
