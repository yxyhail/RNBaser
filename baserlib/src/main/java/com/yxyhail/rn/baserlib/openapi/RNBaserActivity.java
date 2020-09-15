package com.yxyhail.rn.baserlib.openapi;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.yxyhail.rn.baserlib.CustomReactActivityDelegate;

public class RNBaserActivity extends ReactActivity {

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new CustomReactActivityDelegate(this, getMainComponentName()) {
            @Override
            protected ReactRootView createRootView() {
                return createRNRootView();
            }
        };
    }

    public ReactRootView createRNRootView() {
        return new ReactRootView(RNBaserActivity.this);
    }
}
