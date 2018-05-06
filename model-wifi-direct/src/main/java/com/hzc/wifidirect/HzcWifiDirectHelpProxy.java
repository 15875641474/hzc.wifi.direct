package com.hzc.wifidirect;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by huangzongcheng on 2018/5/6.20:59
 * 328854225@qq.com
 */
public class HzcWifiDirectHelpProxy implements InvocationHandler {

    IHzcWifiDirectHelp iHzcWifiDirectHelp;

    public HzcWifiDirectHelpProxy(IHzcWifiDirectHelp iHzcWifiDirectHelp) {
        this.iHzcWifiDirectHelp = iHzcWifiDirectHelp;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        //未初始化或者wifi功能被禁用
        if (method.isAnnotationPresent(BusnessCheck.class)) {
            if (!iHzcWifiDirectHelp.canBusiness()) {
                Log.i("HzcWifiDirectHelpImpl", "not init or wifi-direct disable");
                return null;
            }
        }
        method.invoke(iHzcWifiDirectHelp, objects);
        return null;
    }
}
