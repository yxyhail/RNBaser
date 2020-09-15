package com.yxyhail.rn.baserlib.openapi;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactPackage;

import java.util.List;
import java.util.Map;

public interface RNBaserApiApplication extends ReactApplication {
    Map<String, Object> getConstants();
    List<ReactPackage> getPackageList();
    String getJSBundleFile();
    Boolean isDev();
    String[] preLoadModule();
}
