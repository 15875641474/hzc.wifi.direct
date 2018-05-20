package com.hzc.wifidirect;

import android.net.wifi.p2p.WifiP2pDeviceList;

/**
 * Created by huangzongcheng on 2018/5/6.19:18
 * 328854225@qq.com
 */
public abstract class HzcWifiDirectListener {
    abstract void searchDevicesSuccess();

    public void searchDevicesFailure(int i) {

    }

    public void checkedWifiDeviceSupport(boolean support) {

    }

    public abstract void onGetDevicesList(WifiP2pDeviceList wifiP2pDeviceList);

    public abstract void connectionDeviceSuccess();

    public void connectionDeviceFailure(int i) {

    }

    public abstract void disConnectionSuccess();

    public void disConnectionFailure(int i) {

    }
}
