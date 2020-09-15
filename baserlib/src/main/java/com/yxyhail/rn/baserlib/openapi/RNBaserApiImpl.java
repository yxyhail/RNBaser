package com.yxyhail.rn.baserlib.openapi;

import android.app.Application;
import android.content.Context;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.yxyhail.rn.baserlib.RNCacheViewManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNBaserApiImpl implements RNBaserApi {
    private volatile static RNBaserApiImpl instance;
    private ReactNativeHost nativeHost;
    private RNBaserApiApplication configuration;
    private Map<String, Object> cacheConstants = new HashMap<>();

    private RNBaserApiImpl() {
    }

    static RNBaserApi init() {
        if (instance == null) {
            synchronized (RNBaserApiImpl.class) {
                if (instance == null) {
                    instance = new RNBaserApiImpl();
                }
            }
        }
        return instance;
    }

    public void onAppAttach(Application application) {
        this.configuration = (RNBaserApiApplication) application;
        nativeHost = new ReactNativeHost(application) {
            @Override
            public boolean getUseDeveloperSupport() {
                return configuration.isDev();
            }

            @Override
            protected String getJSBundleFile() {
                return configuration.getJSBundleFile();
//                return "http://192.168.63.209:8081/index.bundle?platform=android&dev=true";
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return configuration.getPackageList();
            }

            @Override
            protected String getJSMainModuleName() {
                return "index";
            }
        };
    }

    @Override
    public void onAppCreate(Application application) {
        SoLoader.init(application, /* native exopackage */ false);
        ReactInstanceManager manager = ((ReactApplication) application).getReactNativeHost().getReactInstanceManager();
        if (!manager.hasStartedCreatingInitialContext()) {
            manager.createReactContextInBackground();
        }
        RNCacheViewManager.getInstance().init(
                ((Application) configuration).getApplicationContext(), null,
                configuration.preLoadModule()
        );
    }


    @Override
    public ReactNativeHost getNativeHost() {
        return nativeHost;
    }

    @Override
    public void open(Context context, String token) {
        setToken(token);
//        WindowIntent.open(context, MainActivity.class);
    }

    @Override
    public void open(Context context) {
        open(context, null);
    }

    @Override
    public Class<?> getRNBaserMain() {
//        return MainActivity.class;
        return null;
    }


    @Override
    public void setToken(String token) {
        cacheConstants.put(RNBaserApi.TOKEN, token);
    }

    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = configuration.getConstants();
        cacheConstants.putAll(constants);
        return cacheConstants;
    }

    @Override
    public void setConstants(Map<String, Object> constants) {
        cacheConstants.putAll(constants);
    }

    @Override
    public String getWxAppId() {
        Map<String, Object> constants = getConstants();
        String wxAppId = (String) constants.get(RNBaserApiImpl.WxAppId);
        return wxAppId;
    }

    @Override
    public void onMainDestroy() {
        RNCacheViewManager.getInstance().onDestroy();
    }
}
