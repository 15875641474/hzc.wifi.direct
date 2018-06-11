package com.hzc.wifidirect;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by huangzongcheng on 2018/5/6.20:54
 * 328854225@qq.com
 */
public interface IHzcWifiDirectHelp {
    /**
     * 启动
     */
    @BusnessCheck
    void startSearch();

    /**
     * 链接设备
     *
     * @param device
     */
    @BusnessCheck
    void connectionDevice(WifiP2pDevice device);

    /**
     * 断开链接
     */
    @BusnessCheck
    void disConnection();

    /**
     * 初始化
     *
     * @param activity
     */
    void init(Activity activity);

    /**
     * 取消初始化
     */
    void unInit();

    /**
     * 停止搜索
     */
    @BusnessCheck
    void stopSearch();

    /**
     * 是否启用
     *
     * @return
     */
    @BusnessCheck
    boolean isEnabled();

    /**
     * 能否进行操作
     *
     * @return
     */
    boolean canBusiness();

    void setOnCheckedWifiDeviceSupportListener(HzcWifiDirectHelpImpl.OnCheckedWifiDeviceSupportListener onCheckedWifiDeviceSupportListener);

    void setOnConnectionDeviceSuccessListener(HzcWifiDirectHelpImpl.OnConnectionDeviceSuccessListener onConnectionDeviceSuccessListener);

    void setOnDisConnectionListener(HzcWifiDirectHelpImpl.OnDisConnectionListener onDisConnectionListener);

    void setOnScanDevicesListener(HzcWifiDirectHelpImpl.OnScanDevicesListener onScanDevicesListener);

    void setOnScanStartListener(HzcWifiDirectHelpImpl.OnScanStartListener onScanStartListener);

    void setOnScaningListener(HzcWifiDirectHelpImpl.OnScaningListener onScaningListener);

    void setOnScanStopListener(HzcWifiDirectHelpImpl.OnScanStopListener onScanStopListener);

    void setOnDisConnectionSuccessListener(HzcWifiDirectHelpImpl.OnDisConnectionSuccessListener onDisConnectionSuccessListener);

    void setOnDisConnectionFailureListener(HzcWifiDirectHelpImpl.OnDisConnectionFailureListener onDisConnectionFailureListener);

    void setOnConnectionDeviceFailureListener(HzcWifiDirectHelpImpl.OnConnectionDeviceFailureListener onConnectionDeviceFailureListener);

}
