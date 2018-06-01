package com.hzc.wifidirect;

import android.net.wifi.p2p.WifiP2pDeviceList;

/**
 * Created by huangzongcheng on 2018/5/6.19:18
 * 328854225@qq.com
 */
public abstract class HzcWifiDirectListener {
    public abstract void searchDevicesSuccess();


    public abstract void onGetDevicesList(WifiP2pDeviceList wifiP2pDeviceList);

    public abstract void connectionDeviceSuccess();


    /**
     * 对等网络正在搜索中
     */
    abstract void onScaning();

    /**
     * 停止对等网络的搜索
     */
    abstract void onScanStop();

    public abstract void disConnectionSuccess();

    public void disConnectionFailure(int i) {

    }

    public void connectionDeviceFailure(int i) {

    }

    public void searchDevicesFailure(int i) {

    }

    public void checkedWifiDeviceSupport(boolean support) {

    }
}
