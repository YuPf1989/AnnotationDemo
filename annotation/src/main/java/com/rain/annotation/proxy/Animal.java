package com.rain.annotation.proxy;


/**
 * Author:rain
 * Date:2018/7/5 15:37
 * Description:
 */
public class Animal implements Fly,Run {
    private static final String TAG  = "Animal";
    @Override
    public void fly() {
        System.out.println("animal:fly");
    }

    @Override
    public void run() {
        System.out.println("animal:run");
    }
}
