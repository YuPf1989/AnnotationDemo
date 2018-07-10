package com.rain.annotationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rain.annotation.annotation.RoutAnnotation;

/**
 * Author:rain
 * Date:2018/7/9 10:27
 * Description:
 * 通过apt 打开activity
 */
@RoutAnnotation(name = "activity_two")
public class ActivityTwo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
}
