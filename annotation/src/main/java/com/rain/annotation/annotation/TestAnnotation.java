package com.rain.annotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:rain
 * Date:2018/7/11 9:21
 * Description:
 * apt练习，请结合test_compiler查看
 *
 */
@Target(ElementType.TYPE)// 表示作用于class 类
@Retention(RetentionPolicy.CLASS)// 作用域为编译时
public @interface TestAnnotation {

}
