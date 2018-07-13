package com.rain.annotationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rain.annotation.annotation.BindView;
import com.rain.annotation.annotation.RoutAnnotation;
import com.rain.annotation.annotation.TestAnnotation;
import com.rain.annotation2.utils.InjectActivity;
import com.rain.annotation2.utils.InjectHelper;

/**
 * Author:rain
 * Date:2018/7/9 10:27
 * Description:
 * 通过apt 调用toast
 */
public class ActivityTwo extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        InjectHelper.inject(this);
        if (tv != null) {
            tv.setText("我是bindview的textView");
        }
    }
}
