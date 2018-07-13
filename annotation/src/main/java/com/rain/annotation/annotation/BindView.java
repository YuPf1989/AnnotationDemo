package com.rain.annotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:rain
 * Date:2018/7/12 16:40
 * Description:
 * 在activity中绑定view
 * 与InjectView不同的是使用编译时注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();
}
