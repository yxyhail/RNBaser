package com.yxyhail.rn.baserlib.openapi;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.yxyhail.rn.baserlib.BuildConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNBaserApplication extends MultiDexApplication implements RNBaserApiApplication {
    private static Application app;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RNBaser.onAppAttach(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        RNBaser.onAppCreate(this);
    }

    @Override
    public Boolean isDev() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getJSBundleFile() {
        return null;
    }

        @Override
    public List<ReactPackage> getPackageList() {
        return null;
    }

    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(RNBaserApi.ENV, "release");
        map.put(RNBaserApi.SOURCE, "");
        map.put(RNBaserApi.WxAppId, "");
        map.put(RNBaserApi.TOKEN, "");
        return map;
    }

    @Override
    public String[] preLoadModule() {
        return null;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return RNBaser.getNativeHost();
    }

    public static ReactApplication getReactApp(){
        return (ReactApplication)app;
    }
}
