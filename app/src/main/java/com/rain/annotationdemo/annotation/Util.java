package com.rain.annotationdemo.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author:rain
 * Date:2018/7/6 10:34
 * Description:
 * 执行injectview具体实现逻辑
 */
public class Util {
    /**
     * <p>
     *     1.方法内部首先拿到activity的所有成员变量，
     2.找到有InjectView注解的成员变量，然后可以拿到button的id
     3.通过反射activityClass.getMethod可以拿到findViewById方法
     4.调用findViewById，参数就是id。
     可以看出来最终都是通过findViewById进行控件的实例化
     * </p>
     */
    public static void injectView(Activity activity) {
        if (null == activity) {
            return;
        }
        Class<? extends Activity> aClass = activity.getClass();
        // 获取所有定义的成员变量
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field :
                declaredFields) {
            // 变量是否有注解标记
            if (field.isAnnotationPresent(InjectView.class)) {
                // 获取注解信息
                InjectView annotation = field.getAnnotation(InjectView.class);
                // 获取注解中的值
                int value = annotation.value();
                // 找到findviewById方法
                try {
                    Method findViewById = aClass.getMethod("findViewById", int.class);
                    findViewById.setAccessible(true);
                    findViewById.invoke(activity, value);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理onClick方法
     * 1.首先就是获取activity的所有成员方法getDeclaredMethods
     2.找到有onClick注解的方法，拿到value就是注解点击事件button的id
     3.获取onClick注解的注解EventType的参数，从中可以拿到设定点击事件方法setOnClickListener + 点击事件的监听接口OnClickListener+点击事件的回调方法onClick
     4.在点击事件发生的时候Android系统会触发onClick事件，我们需要将事件的处理回调到注解的方法InvokeBtnClick，也就是代理的思想
     5.通过动态代理Proxy.newProxyInstance实例化一个实现OnClickListener接口的代理，代理会在onClick事件发生的时候回调InvocationHandler进行处理
     6.RealSubject就是activity，因此我们传入ProxyHandler实例化一个InvocationHandler，用来将onClick事件映射到activity中我们注解的方法InvokeBtnClick
     7.通过反射实例化Button，findViewByIdMethod.invoke
     8.通过Button.setOnClickListener(OnClickListener)进行设定点击事件监听。
     */
    public static void injectEvent(Activity activity){
        if (null == activity) {
            return;
        }

        Class<? extends Activity> aClass = activity.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method m :
                declaredMethods) {
            if (m.isAnnotationPresent(onClick.class)) {
                onClick declaredAnnotation = m.getAnnotation(onClick.class);
                // 获取button id
                int[] value = declaredAnnotation.value();
                // 获取event type
                EventType eventType = declaredAnnotation.annotationType().getAnnotation(EventType.class);
                Class listenerType = eventType.listenerType();
                String listenerSetter = eventType.listenerSetter();
                String methodName = eventType.methodName();

                //创建InvocationHandler和动态代理(代理要实现listenerType，这个例子就是处理onClick点击事件)
                ProxyHandler proxyHandler = new ProxyHandler(activity);
                Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyHandler);

                proxyHandler.mapMethod(methodName, m);
                for (int id: value) {
                    // 找到button
                    try {
                        Method findViewById = aClass.getMethod("findViewById", int.class);
                        findViewById.setAccessible(true);
                        View btn = (View) findViewById.invoke(activity, id);
                        // 根据listenerSetter方法名和listenerType方法参数找到method
                        Method setListenerMethod = btn.getClass().getMethod(listenerSetter, listenerType);// 前一个参数是方法名，后一个是参数Class类型
                        setListenerMethod.setAccessible(true);
                        setListenerMethod.invoke(btn, listener);// 前一个是调用者对象，后一个是方法参数名
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }

}
