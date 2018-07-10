package com.rain.annotation_process;

import com.google.auto.service.AutoService;
import com.rain.annotation.annotation.RoutAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 参见
 * https://www.jianshu.com/p/9616f4a462bd
 * java lib :里边只有java文件，打包后是jar
 * Android library:里边不仅有java文件，还允许有资源文件，打包后为aar,并且有安卓lib
 * module图标也有所不同
 * 该类存在问题，未引入到app中
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    // 文件相关的辅助类
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> types = new LinkedHashSet<>();
        types.add(RoutAnnotation.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<String, String> nameMap = new HashMap<>();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(RoutAnnotation.class);
        for (Element e :
                elements) {
            RoutAnnotation annotation = e.getAnnotation(RoutAnnotation.class);
            String name = annotation.name();
            nameMap.put(name, e.getSimpleName().toString());
        }

        //generate Java File
        generateJavaFile(nameMap);
        return true;
    }

    private void generateJavaFile(Map<String, String> nameMap) {
        //generate constructor
        MethodSpec.Builder constructorBuidler = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("routeMap = new $T<>()", HashMap.class);
        for (String key : nameMap.keySet()) {
            String name = nameMap.get(key);
            constructorBuidler.addStatement("routeMap.put(\"$N\", \"$N\")", key, name);
        }
        MethodSpec constructorName = constructorBuidler.build();

        //generate getActivityRouteName method
        MethodSpec routeName = MethodSpec.methodBuilder("getActivityName")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addParameter(String.class, "routeName")
                .beginControlFlow("if (null != routeMap && !routeMap.isEmpty())")
                .addStatement("return (String)routeMap.get(routeName)")
                .endControlFlow().addStatement("return \"\"")
                .build();
        //generate class
        TypeSpec typeSpec = TypeSpec.classBuilder("AnnotationRoute$Finder")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructorName)
                .addMethod(routeName)
                .addSuperinterface(Provider.class)
                .addField(HashMap.class, "routeMap", Modifier.PRIVATE)
                .build();
        JavaFile javaFile = JavaFile.builder("com.rain.annotation.annotaioncompiletest", typeSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
