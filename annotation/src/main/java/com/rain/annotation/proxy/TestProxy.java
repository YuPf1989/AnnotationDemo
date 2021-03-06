package com.rain.annotation.proxy;

import java.lang.reflect.Proxy;

/**
 * Author:rain
 * Date:2018/7/5 16:01
 * Description:
 */
public class TestProxy {
    public static void main(String[] args) {
        Animal animal = new Animal();
        ClassLoader classLoader = animal.getClass().getClassLoader();
        Class<?>[] interfaces = animal.getClass().getInterfaces();
        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, new ProxyHandler(animal));
        Fly fly = (Fly) proxy;
        fly.fly();
        Run run = (Run) proxy;
        run.run();
    }
}
