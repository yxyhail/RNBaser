package com.yxyhail.rn.baserlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

/**
 * A delegate for handling React Application support. This delegate is unaware whether it is used in
 * an {@link Activity} or a {@link android.app.Fragment}.
 */
public class CustomReactDelegate {

    private final Activity mActivity;
    private ReactRootView mReactRootView;

    @Nullable
    private final String mMainComponentName;

    @Nullable
    private Bundle mLaunchOptions;

    @Nullable
    private DoubleTapReloadRecognizer mDoubleTapReloadRecognizer;

    private ReactNativeHost mReactNativeHost;

    public CustomReactDelegate(
            Activity activity,
            ReactNativeHost reactNativeHost,
            @Nullable String appKey,
            @Nullable Bundle launchOptions) {
        mActivity = activity;
        mMainComponentName = appKey;
        mLaunchOptions = launchOptions;
        mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
        mReactNativeHost = reactNativeHost;
    }

    public void onHostResume() {
        if (getReactNativeHost().hasInstance()) {
            if (mActivity instanceof DefaultHardwareBackBtnHandler) {
                getReactNativeHost()
                        .getReactInstanceManager()
                        .onHostResume(mActivity, (DefaultHardwareBackBtnHandler) mActivity);
            } else {
                throw new ClassCastException(
                        "Host Activity does not implement DefaultHardwareBackBtnHandler");
            }
        }
    }

    public void onHostPause() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostPause(mActivity);
        }
    }

    public void onHostDestroy() {
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
        }
        RNCacheViewManager.getInstance().onDestroy();
        RNCacheViewManager.getInstance().init(mActivity.getApplicationContext(), mLaunchOptions, mMainComponentName);
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(mActivity);
        }
    }

    public boolean onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
            return true;
        }
        return false;
    }

    public void onActivityResult(
            int requestCode, int resultCode, Intent data, boolean shouldForwardToReactInstance) {
        if (getReactNativeHost().hasInstance() && shouldForwardToReactInstance) {
            getReactNativeHost()
                    .getReactInstanceManager()
                    .onActivityResult(mActivity, requestCode, resultCode, data);
        }
    }

    public void loadApp() {
        loadApp(mMainComponentName);
    }

    public void loadApp(String appKey) {
        if (mReactRootView != null) {
            throw new IllegalStateException("Cannot loadApp while app is already running.");
        }
        if (appKey == null) {
            FLog.e(ReactConstants.TAG, "mainComponentName must not be null!");
            return;
        }
        ReactRootView tmpRootView = RNCacheViewManager.getInstance().getRootView(appKey);
        try {
            ViewParent viewParent = tmpRootView.getParent();
            if (viewParent != null) {
                ViewGroup vp = (ViewGroup) viewParent;
                vp.removeView(tmpRootView);
            }
            mReactRootView = createRootView();
            mReactRootView.startReactApplication(
                    getReactNativeHost().getReactInstanceManager(), appKey, mLaunchOptions);
            mActivity.setContentView(mReactRootView);
            mActivity.addContentView(tmpRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            new Handler(mActivity.getMainLooper()).postDelayed(() -> {
                tmpRootView.unmountReactApplication();
                ViewParent viewParent1 = tmpRootView.getParent();
                if (viewParent1 != null) {
                    ViewGroup vp = (ViewGroup) viewParent1;
                    vp.removeView(tmpRootView);
                }
            }, 2500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReactRootView getReactRootView() {
        return mReactRootView;
    }

    protected ReactRootView createRootView() {
        return new ReactRootView(mActivity);
    }

    /**
     * Handles delegating the {@link Activity#onKeyUp(int, KeyEvent)} method to determine whether the
     * application should show the developer menu or should reload the React Application.
     *
     * @return true if we consume the event and either shoed the develop menu or reloaded the
     * application.
     */
    public boolean shouldShowDevMenuOrReload(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance() && getReactNativeHost().getUseDeveloperSupport()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
                return true;
            }
            boolean didDoubleTapR =
                    Assertions.assertNotNull(mDoubleTapReloadRecognizer)
                            .didDoubleTapR(keyCode, mActivity.getCurrentFocus());
            if (didDoubleTapR) {
                getReactNativeHost().getReactInstanceManager().getDevSupportManager().handleReloadJS();
                return true;
            }
        }
        return false;
    }

    /**
     * Get the {@link ReactNativeHost} used by this app.
     */
    private ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }
}