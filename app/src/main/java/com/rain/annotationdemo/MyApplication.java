package com.rain.annotationdemo;

import android.app.Application;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Author:rain
 * Date:2018/7/11 9:47
 * Description:
 */
public class MyApplication extends Application {
    private static Application mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return true;
            }
        });
    }

    public static Application getApplication(){
        return mApp;
    }
}
