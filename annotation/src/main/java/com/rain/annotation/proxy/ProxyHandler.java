package com.rain.annotation.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author:rain
 * Date:2018/7/5 15:45
 * Description:
 */
public class ProxyHandler implements InvocationHandler {
    private static final String TAG  = "ProxyHandler";
    private Animal animal;
    public ProxyHandler(Animal animal) {
        this.animal = animal;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("invoke: "+method.getName());
        return method.invoke(animal,args);
    }
}
