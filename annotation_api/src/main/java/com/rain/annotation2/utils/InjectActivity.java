package com.rain.annotation2.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Author:rain
 * Date:2018/7/11 10:57
 * Description:
 * 联合TestAnnotation,test_compiler查看
 * 在运行时通过反射调用生成build的类
 */
public class InjectActivity {
    private static final String TAG  = "InjectActivity";
    // 缓存通过反射获得的类
    public static final ArrayMap<String, Object> injectMap = new ArrayMap<>();

    public static void inject(AppCompatActivity activity) {
        try {
            String name = activity.getClass().getName();
            Object inject = injectMap.get(name);
            if (inject == null) {
                // 加载build生成的类
                // TODO: 2018/7/11 $$表示内部类?
                Class<?> aClass = Class.forName(name + "$$InjectActivity");
                inject = aClass.newInstance();
                injectMap.put(name, inject);
            }
            //反射执行build中的方法
            Method method = inject.getClass().getDeclaredMethod("inject", activity.getClass());
            method.invoke(inject, activity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "e: "+e.toString() );
        }
    }
}

