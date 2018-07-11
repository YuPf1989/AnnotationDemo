package com.rain.test_compiler;

import com.google.auto.service.AutoService;
import com.rain.annotation.annotation.TestAnnotation;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Author:rain
 * Date:2018/7/11 9:28
 * Description:
 * 注解处理器
 */
@AutoService(Processor.class)// 这个注解是谷歌提供了，快速实现注解处理器，会帮你生成配置文件啥的 。直接用就好
public class ActivityInjectProcesser extends AbstractProcessor {

    private Filer filer;// 文件相关的辅助类
    private Elements elementUtils;// 元素相关的辅助类
    private TreeMap<String, AnnotatedClass> annotatedClassMap;// 缓存通过反射查找的注解的类，减少反射使用
    private Messager messager;// 日志相关的辅助类

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> types = new LinkedHashSet<>();
        types.add(TestAnnotation.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        annotatedClassMap = new TreeMap<>();
    }

    // 该方法是核心方法，在这里处理的你的业务。检测类别参数，生成java文件等
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        annotatedClassMap.clear();
        // 查找TestAnnotation注解标记的元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TestAnnotation.class);
        for (Element e :
                elements) {
            // 获取注解element为class的
            if (e.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) e;
                String fullName = typeElement.getQualifiedName().toString();
                System.out.println("fullName:"+fullName);
                AnnotatedClass annotatedClass = annotatedClassMap.get(fullName);
                if (annotatedClass == null) {
                    // 生成java文件
                    AnnotatedClass aClass = new AnnotatedClass(typeElement, elementUtils, messager);
                    annotatedClassMap.put(fullName, aClass);
                }
            }
        }

        // 将生成后的文件写入到原先类中
        for (AnnotatedClass annotatedClass : annotatedClassMap.values()) {
            try {
                annotatedClass.generateActivityFile().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }
}
