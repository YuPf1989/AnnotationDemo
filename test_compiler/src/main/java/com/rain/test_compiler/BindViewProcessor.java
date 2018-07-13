package com.rain.test_compiler;

import com.google.auto.service.AutoService;
import com.rain.annotation.annotation.BindView;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Author:rain
 * Date:2018/7/12 16:43
 * Description:
 * bindView注解处理器
 * 参见
 * https://www.jianshu.com/p/9e34defcb76f
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    // 存放同一个Class下的所有注解信息
    private Map<String, List<VariableInfo>> classMap = new HashMap<>();
    // 存放Class对应的信息：
    private Map<String, TypeElement> classTypeElement = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();

    }

    /**
     * 该方法遇到一个编译时注解就会执行一次
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 1.
        collectInfo(roundEnvironment);
        // 2.
        writeToFile();
        return true;
    }

    /**
     * 收集 Class 内的所有被 @BindView 注解的成员变量；
     */
    private void collectInfo(RoundEnvironment roundEnvironment) {
        classMap.clear();
        classTypeElement.clear();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element e :
                elements) {
            // 获取bindview注解View的值
            int viewId = e.getAnnotation(BindView.class).value();
            // 代表被注解的元素
            VariableElement variableElement = (VariableElement) e;

            // 注解元素所在的class
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            // class的完整路径
            String classFullName = typeElement.getQualifiedName().toString();
            // 收集class中所有被注解的元素
            List<VariableInfo> variableInfos = classMap.get(classFullName);
            if (variableInfos == null) {
                variableInfos = new ArrayList<>();
                classMap.put(classFullName, variableInfos);

                // 保存Class对应要素（名称、完整路径等）
                classTypeElement.put(classFullName, typeElement);
            }

            VariableInfo variableInfo = new VariableInfo();
            variableInfo.variableElement = variableElement;
            variableInfo.viewId = viewId;
            variableInfos.add(variableInfo);

        }
    }

    /**
     * 根据上一步收集的内容，生成 .java 源文件
     */
    private void writeToFile() {
        for (String classFullName :
                classMap.keySet()) {
            TypeElement typeElement = classTypeElement.get(classFullName);

            // 使用构造函数绑定数据
            MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(TypeName.get(typeElement.asType()), "activity").build());
            List<VariableInfo> variableInfos = classMap.get(classFullName);
            for (VariableInfo variableInfo:
                 variableInfos) {
                VariableElement variableElement = variableInfo.variableElement;
                int viewId = variableInfo.viewId;
                // 变量名称(比如：TextView tv 的 tv)
                String variableName  = variableElement.getSimpleName().toString();
                // 变量类型的完整类路径（比如：android.widget.TextView）
                String variableFullName  = variableElement.asType().toString();
                // 在构造方法中增加赋值语句，例如：activity.tv = (android.widget.TextView)activity.findViewById(215334);
                constructor.addStatement("activity.$L=($L)activity.findViewById($L)", variableName, variableFullName, viewId);
            }

            // 构建class
            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName() + "$$bindView")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(constructor.build())
                    .build();

            // 与目标Class放在同一个包下，解决Class属性的可访问性
            String packageFullName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            JavaFile javaFile = JavaFile.builder(packageFullName, typeSpec).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
