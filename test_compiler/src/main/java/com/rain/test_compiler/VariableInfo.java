package com.rain.test_compiler;

import javax.lang.model.element.VariableElement;

/**
 * Author:rain
 * Date:2018/7/13 10:44
 * Description:
 * 注解标记的变量信息
 */
public class VariableInfo {
    // 被注解 View 的 Id 值
    int viewId;
    // 被注解 View 的信息：变量名称、类型
    VariableElement variableElement;
}
