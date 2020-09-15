package com.yxyhail.rn.baserlib.openapi;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.facebook.react.ReactNativeHost;

import java.util.Map;

public class RNBaser {

    public static RNBaserApi api() {
        return RNBaserApiImpl.init();
    }

    public static void onAppAttach(Application application) {
        api().onAppAttach(application);
    }

    public static void onAppCreate(Application application) {
        api().onAppCreate(application);
    }

    public static ReactNativeHost getNativeHost() {
        return api().getNativeHost();
    }

    public static void open(Context context, String ticket) {
        api().open(context, ticket);
    }

    public static void open(Context context) {
        api().open(context);
    }

//    public static Class<?> getRNBaserMain() {
//        return api().getRNBaserMain();
//    }

    public static Map<String, Object> getConstants() {
        return api().getConstants();
    }

    public static void setConstants(Map<String, Object> constants) {
        api().setConstants(constants);
    }

    public static String getWxAppId() {
        return api().getWxAppId();
    }

//
//    public static boolean onWxResp(Activity activity, BaseResp baseResp) {
//        return WxUtils.onWxResp(activity,baseResp);
//    }
//
//    public static void onWxReq(Activity activity, BaseReq baseReq) {
//        WxUtils.onWxReq(activity, baseReq);
//    }

    public static boolean isThirdParty() {
        String source = (String) api().getConstants().get(RNBaserApi.SOURCE);
        return !TextUtils.isEmpty(source);
    }

    public static void onMainDestroy() {
        api().onMainDestroy();
    }

}
