package com.rain.annotation.proxy;

/**
 * Author:rain
 * Date:2018/7/5 15:38
 * Description:
 */
public class AnimalProxy implements Fly,Run{
    private Animal animal;
    public AnimalProxy(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void fly() {
        animal.fly();
    }

    @Override
    public void run() {
        animal.run();
    }
}
