package com.rain.annotationdemo.annotation;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Author:rain
 * Date:2018/7/6 11:18
 * Description:
 */
public class ProxyHandler implements InvocationHandler {
    private HashMap<String, Method> mMethodHashMap;
    private WeakReference<Activity> mHandlerRef;


    public ProxyHandler(Activity activity) {
        mHandlerRef = new WeakReference<>(activity);
        mMethodHashMap = new HashMap<>();
    }

    public void mapMethod(String name, Method method) {
        mMethodHashMap.put(name, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object handler = mHandlerRef.get();

        if (null == handler) return null;

        String name = method.getName();

        //将onClick方法的调用映射到activity 中的InvokeBtnClick()方法
        Method realMethod = mMethodHashMap.get(name);
        if (null != realMethod){
            return realMethod.invoke(handler, args);
        }
        return null;
    }
}
