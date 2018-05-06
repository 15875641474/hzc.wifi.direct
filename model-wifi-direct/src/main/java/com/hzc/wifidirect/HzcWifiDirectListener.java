package com.hzc.wifidirect;

import android.net.wifi.p2p.WifiP2pDeviceList;

/**
 * Created by huangzongcheng on 2018/5/6.19:18
 * 328854225@qq.com
 */
public abstract class HzcWifiDirectListener {
    abstract void searchDevicesSuccess();

    void searchDevicesFailure(int i) {

    }

    void checkedWifiDeviceSupport(boolean support) {

    }

    abstract void onGetDevicesList(WifiP2pDeviceList wifiP2pDeviceList);

    abstract void connectionDeviceSuccess();

    void connectionDeviceFailure(int i) {

    }

    abstract void disConnectionSuccess();

    void disConnectionFailure(int i) {

    }
}
