package com.yxyhail.rn.baserlib;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewParent;

import com.facebook.common.logging.FLog;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.ReactConstants;

import java.util.Map;
import java.util.WeakHashMap;

public class RNCacheViewManager {
    public Map<String, ReactRootView> CACHE;

    private RNCacheViewManager() {
    }

    private static class SingletonHolder {
        private final static RNCacheViewManager INSTANCE = new RNCacheViewManager();
    }

    public static RNCacheViewManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ReactRootView getRootView(String moduleName) {
        if (CACHE == null) return null;
        return CACHE.get(moduleName);
    }

    public ReactNativeHost getReactNativeHost(Context context) {
        return ((ReactApplication) context.getApplicationContext()).getReactNativeHost();
    }

    public void init(Context context, Bundle launchOptions, String... moduleNames) {
        if (CACHE == null) CACHE = new WeakHashMap<>();
        for (String moduleName : moduleNames) {
            ReactRootView rootView = new ReactRootView(context);
            rootView.startReactApplication(
                    getReactNativeHost(context).getReactInstanceManager(),
                    moduleName,
                    launchOptions);
            CACHE.remove(moduleName);
            CACHE.put(moduleName, rootView);
            FLog.i(ReactConstants.TAG, moduleName + " has preload");
        }
    }

    public void onDestroyOne(String componentName) {
        try {
            ReactRootView reactRootView = CACHE.get(componentName);
            if (reactRootView != null) {
                ViewParent parent = reactRootView.getParent();
                if (parent != null) {
                    ((android.view.ViewGroup) parent).removeView(reactRootView);
                }
                reactRootView.unmountReactApplication();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            for (Map.Entry<String, ReactRootView> entry : CACHE.entrySet()) {
                ReactRootView reactRootView = entry.getValue();
                ViewParent parent = reactRootView.getParent();
                if (parent != null) {
                    ((android.view.ViewGroup) parent).removeView(reactRootView);
                }
                reactRootView.unmountReactApplication();
                reactRootView = null;
            }
            CACHE.clear();
            CACHE = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}