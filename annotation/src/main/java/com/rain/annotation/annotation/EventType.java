package com.rain.annotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:rain
 * Date:2018/7/6 10:18
 * Description:
 * 该java lib主要是定义注解
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {
    Class listenerType();
    String listenerSetter();
    String methodName();
}
