### What this ?

it is wifi-direct for android.

i just make it easy to use

okway,, we say chinese

其实这个就是把wifi-direct的整个流程用接口包装起来
你们不用关心怎么跑，只管用`IHzcWifiDirectHelp`这个接口类提供的接口就能实现简单直接的wifi-direct链接
接口类的实现采用线程互斥 + 动态代理  

值得注意的是，华为8.0+的手机只能被搜索而不能搜索出P2P设备


### How to Ues ?

in gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.15875641474:hzc.wifi.direct:v1.0.12'
}
```

### USE
```
IHzcWifiDirectHelp xx = HzcWifiDirectHelpImpl.newInstance();
IHzcWifiDirectHelp.init
IHzcWifiDirectHelp.startSearch
IHzcWifiDirectHelp.connectionDevice
```
just done，so easy !!

### API
Lock at the class `IHzcWifiDirectHelp` , it has describe.
