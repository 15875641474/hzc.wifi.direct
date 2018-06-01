package com.hzc.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static com.hzc.wifidirect.HzcWifiDirectHelpImpl.InnerObject.innerObject;

/**
 * Created by huangzongcheng on 2018/5/6.19:14
 * 328854225@qq.com
 */
public class HzcWifiDirectHelpImpl implements IHzcWifiDirectHelp {

    private static final String tag = "HzcWifiDirectHelpImpl";

    boolean isEnabled = false, isInit = false;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifiChannel;
    Activity activity;
    BroadcaseWifiDirect broadcaseWifiDirect;
    private static IHzcWifiDirectHelp iHzcWifiDirectHelp = InnerObject.innerObject;


    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean canBusiness() {
        return isEnabled & isInit;
    }


    public static IHzcWifiDirectHelp newInstance() {
        return iHzcWifiDirectHelp;
    }

    private HzcWifiDirectHelpImpl() {
    }

    /**
     * 线程互斥 + 动态代理模式进行加载类
     */
    static class InnerObject {
        static IHzcWifiDirectHelp innerObject;

        static {
            innerObject = new HzcWifiDirectHelpImpl();
            InvocationHandler handler = new HzcWifiDirectHelpProxy(innerObject);
            innerObject = (IHzcWifiDirectHelp) Proxy.newProxyInstance(handler.getClass().getClassLoader(), innerObject.getClass().getInterfaces(), handler);
        }
    }

    /**
     * 取消初始化
     */
    @Override
    public void unInit() {
        if (activity == null)
            return;
        activity.unregisterReceiver(broadcaseWifiDirect);
        wifiP2pManager.stopPeerDiscovery(wifiChannel, null);
        wifiP2pManager.removeGroup(wifiChannel, null);
        wifiChannel = null;
        wifiP2pManager = null;
        activity = null;
        broadcaseWifiDirect = null;
        innerObject = null;
        isInit = false;
        Log.i(tag, "uninit success");
    }

    /**
     * 停止搜索
     */
    @Override
    public void stopSearch() {
        Log.i(tag, "stop search");
        wifiP2pManager.stopPeerDiscovery(wifiChannel, null);
    }

    /**
     * 初始化
     *
     * @param activity
     */
    @Override
    public void init(Activity activity) {
        this.activity = activity;
        wifiP2pManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        wifiChannel = wifiP2pManager.initialize(activity, activity.getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {

            }
        });

        //加入wifidirect的广播监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        broadcaseWifiDirect = new BroadcaseWifiDirect();
        activity.registerReceiver(broadcaseWifiDirect, intentFilter);
        isInit = true;
        Log.i(tag, "add broadcase[WIFI_P2P_STATE_CHANGED_ACTION,WIFI_P2P_PEERS_CHANGED_ACTION,WIFI_P2P_CONNECTION_CHANGED_ACTION,WIFI_P2P_THIS_DEVICE_CHANGED_ACTION]");
        Log.i(tag, "init success");
    }

    /**
     * 断开链接
     */
    @Override
    public void disConnection() {
        wifiP2pManager.removeGroup(wifiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(tag, "disConnection success");
                if (onDisConnectionSuccessListener != null) {
                    onDisConnectionSuccessListener.disConnectionSuccess();
                }
            }

            @Override
            public void onFailure(int i) {
                Log.i(tag, "disConnection failure");
                if (onDisConnectionFailureListener != null) {
                    onDisConnectionFailureListener.disConnectionFailure(i);
                }
            }
        });
    }

    /**
     * 链接设备
     *
     * @param device
     */
    @Override
    public void connectionDevice(final WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        wifiP2pManager.connect(wifiChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(tag, String.format("connectionDevice success [%s.%s]", device.deviceName, device.deviceAddress));
            }

            @Override
            public void onFailure(int i) {
                Log.i(tag, String.format("connectionDevice failure [%s.%s]", device.deviceName, device.deviceAddress));
            }
        });
    }

    /**
     * 开启搜索
     */
    public void startSearch() {
        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(tag, "started search device");
                if (onSearchDevicesSuccessListener != null) {
                    onSearchDevicesSuccessListener.searchDevicesSuccess();
                }
            }

            @Override
            public void onFailure(int i) {
                Log.i(tag, "startSearch search device error");
                if (onSearchDevicesFailureListener != null) {
                    onSearchDevicesFailureListener.searchDevicesFailure(i);
                }
            }
        });
    }

    /**
     * 广播事件监听
     */
    public class BroadcaseWifiDirect extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(tag, "broadcase.action=" + intent.getAction());
            switch (intent.getAction()) {
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION: {//P2P设备启用状态
                    boolean support = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1) == WifiP2pManager.WIFI_P2P_STATE_ENABLED;
                    isEnabled = support;
                    if (onCheckedWifiDeviceSupportListener != null) {
                        onCheckedWifiDeviceSupportListener.checkedWifiDeviceSupport(support);
                    }
                }
                break;
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION: {//P2P对等网络列表发生改变
                    wifiP2pManager.requestPeers(wifiChannel, new WifiP2pManager.PeerListListener() {
                        //获得设备列表
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            if (onGetDevicesListListener != null) {
                                onGetDevicesListListener.onGetDevicesList(wifiP2pDeviceList);
                            }
                        }
                    });
                }
                break;
                case WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION: {//是否查找状态
                    int State = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, -1);
                    if (State == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {//已开启搜索
                        if (onScaningListener != null) {
                            onScaningListener.onScaning();
                        }
                    } else if (State == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED) {//已停止搜索
                        if (onScanStopListener != null) {
                            onScanStopListener.onScanStop();
                        }
                    }
                }
                break;
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION: {//wifi链接状态
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected()) {
                        WifiP2pInfo wifip2pinfo = (WifiP2pInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                        if (onConnectionDeviceSuccessListener != null) {
                            onConnectionDeviceSuccessListener.connectionDeviceSuccess(networkInfo, wifip2pinfo);
                        }
                    } else {
                        if (onDisConnectionListener != null) {
                            onDisConnectionListener.disConnection(networkInfo);
                        }
                    }
                }
                break;
            }
        }
    }


    OnCheckedWifiDeviceSupportListener onCheckedWifiDeviceSupportListener;
    OnConnectionDeviceSuccessListener onConnectionDeviceSuccessListener;
    OnDisConnectionListener onDisConnectionListener;
    OnGetDevicesListListener onGetDevicesListListener;
    OnSearchDevicesSuccessListener onSearchDevicesSuccessListener;
    OnScaningListener onScaningListener;
    OnScanStopListener onScanStopListener;
    OnDisConnectionSuccessListener onDisConnectionSuccessListener;
    OnDisConnectionFailureListener onDisConnectionFailureListener;
    OnConnectionDeviceFailureListener onConnectionDeviceFailureListener;
    OnSearchDevicesFailureListener onSearchDevicesFailureListener;

    public interface OnCheckedWifiDeviceSupportListener {
        void checkedWifiDeviceSupport(boolean support);
    }

    public interface OnConnectionDeviceSuccessListener {
        void connectionDeviceSuccess(NetworkInfo networkInfo, WifiP2pInfo wifip2pinfo);
    }

    public interface OnDisConnectionListener {
        void disConnection(NetworkInfo networkInfo);
    }

    public interface OnGetDevicesListListener {
        void onGetDevicesList(WifiP2pDeviceList wifiP2pDeviceList);
    }

    public interface OnSearchDevicesSuccessListener {
        void searchDevicesSuccess();
    }

    public interface OnScaningListener {
        void onScaning();
    }

    public interface OnScanStopListener {
        void onScanStop();
    }

    public interface OnDisConnectionSuccessListener {
        void disConnectionSuccess();
    }

    public interface OnDisConnectionFailureListener {
        void disConnectionFailure(int i);
    }

    public interface OnConnectionDeviceFailureListener {
        void connectionDeviceFailure(int i);
    }

    public interface OnSearchDevicesFailureListener {
        void searchDevicesFailure(int i);
    }

    public void setOnCheckedWifiDeviceSupportListener(OnCheckedWifiDeviceSupportListener onCheckedWifiDeviceSupportListener) {
        this.onCheckedWifiDeviceSupportListener = onCheckedWifiDeviceSupportListener;
    }

    public void setOnConnectionDeviceSuccessListener(OnConnectionDeviceSuccessListener onConnectionDeviceSuccessListener) {
        this.onConnectionDeviceSuccessListener = onConnectionDeviceSuccessListener;
    }

    public void setOnDisConnectionListener(OnDisConnectionListener onDisConnectionListener) {
        this.onDisConnectionListener = onDisConnectionListener;
    }

    public void setOnGetDevicesListListener(OnGetDevicesListListener onGetDevicesListListener) {
        this.onGetDevicesListListener = onGetDevicesListListener;
    }

    public void setOnSearchDevicesSuccessListener(OnSearchDevicesSuccessListener onSearchDevicesSuccessListener) {
        this.onSearchDevicesSuccessListener = onSearchDevicesSuccessListener;
    }

    public void setOnScaningListener(OnScaningListener onScaningListener) {
        this.onScaningListener = onScaningListener;
    }

    public void setOnScanStopListener(OnScanStopListener onScanStopListener) {
        this.onScanStopListener = onScanStopListener;
    }

    public void setOnDisConnectionSuccessListener(OnDisConnectionSuccessListener onDisConnectionSuccessListener) {
        this.onDisConnectionSuccessListener = onDisConnectionSuccessListener;
    }

    public void setOnDisConnectionFailureListener(OnDisConnectionFailureListener onDisConnectionFailureListener) {
        this.onDisConnectionFailureListener = onDisConnectionFailureListener;
    }

    public void setOnConnectionDeviceFailureListener(OnConnectionDeviceFailureListener onConnectionDeviceFailureListener) {
        this.onConnectionDeviceFailureListener = onConnectionDeviceFailureListener;
    }

    public void setOnSearchDevicesFailureListener(OnSearchDevicesFailureListener onSearchDevicesFailureListener) {
        this.onSearchDevicesFailureListener = onSearchDevicesFailureListener;
    }
}
