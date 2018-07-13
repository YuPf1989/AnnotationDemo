package com.rain.annotation2.utils;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * Author:rain
 * Date:2018/7/13 11:38
 * Description:
 * 执行生成bindView java中的
 */
public class InjectHelper {
    public static void inject(Activity host) {
        try {
            String classFullName = host.getClass().getName() + "$$bindView";
            Class<?> proxy  = Class.forName(classFullName);
            Constructor<?> constructor = proxy.getConstructor(host.getClass());
            constructor.newInstance(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
