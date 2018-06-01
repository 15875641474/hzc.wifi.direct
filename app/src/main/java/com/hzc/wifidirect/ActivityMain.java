package com.hzc.wifidirect;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    List<WifiP2pDevice> wifiP2pDevices = new ArrayList<>();
    IHzcWifiDirectHelp iHzcWifiDirectHelp;
    private android.widget.Button btnsearch;
    private android.widget.Button btndisconnection;
    private android.support.v7.widget.RecyclerView recyclerview;

    class Holder extends RecyclerView.ViewHolder {

        TextView tvName;

        public Holder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(getLayoutInflater().inflate(R.layout.item_devices, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            holder.tvName.setText(wifiP2pDevices.get(position).deviceName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //链接设备
                    iHzcWifiDirectHelp.connectionDevice(wifiP2pDevices.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return wifiP2pDevices.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnuninit = (Button) findViewById(R.id.btn_uninit);
        Button btninit = (Button) findViewById(R.id.btn_init);
        this.recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        this.btndisconnection = (Button) findViewById(R.id.btn_disconnection);
        this.btnsearch = (Button) findViewById(R.id.btn_search);
        findViewById(R.id.btn_unsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHzcWifiDirectHelp.stopSearch();
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        btninit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHzcWifiDirectHelp.init(ActivityMain.this);
                iHzcWifiDirectHelp.setOnGetDevicesListListener(new HzcWifiDirectHelpImpl.OnGetDevicesListListener() {
                    @Override
                    public void onGetDevicesList(WifiP2pDeviceList wifiP2pDeviceList) {
                        wifiP2pDevices.clear();
                        for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                            wifiP2pDevices.add(device);
                        }
                        recyclerview.setAdapter(new Adapter());
                    }
                });
            }
        });

        btnuninit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHzcWifiDirectHelp.unInit();
            }
        });

        iHzcWifiDirectHelp = HzcWifiDirectHelpImpl.newInstance();

        //断开
        btndisconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHzcWifiDirectHelp.disConnection();
            }
        });

        //搜索
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHzcWifiDirectHelp.startSearch();
            }
        });

    }
}

