package com.yxyhail.rn.baserlib.openapi;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;

import java.util.Map;

interface RNBaserApi {

    String ENV = "buildEnv";
    String SOURCE = "appSource";
    String TOKEN = "token";
    String WxAppId = "wxAppId";

    String VCODE = "appBuild";
    String VNAME = "appVersion";

    void onAppAttach(Application application);

    void onAppCreate(Application application);

    ReactNativeHost getNativeHost();

    void open(Context context, String token);

    void open(Context context);

    Class<?> getRNBaserMain();

    void setToken(String token);

    Map<String, Object> getConstants();

    void setConstants(Map<String, Object> constants);

    String getWxAppId();

    void onMainDestroy();

}
