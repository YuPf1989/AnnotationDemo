package com.rain.annotationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rain.annotationdemo.annotation.InjectView;
import com.rain.annotationdemo.annotation.Util;
import com.rain.annotationdemo.annotation.onClick;

/**
 * 练习注解、反射、动态代理
 * 参见
 * 动态代理：https://www.jianshu.com/p/b00ef12d53cc
 * 反射：https://www.jianshu.com/p/fad15887a05e
 * 测试自定义注解
 */
public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.btn_bindClick)
    Button btn_bindClick;
    @InjectView(R.id.btn_bindView)
    Button btn_bindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.injectView(this);
        Util.injectEvent(this);


    }

    @onClick({R.id.btn_bindClick, R.id.btn_bindView})
    public void onClick(View view) {
        Toast.makeText(this, "" + view.getId(), Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.btn_bindClick:
                break;

            case R.id.btn_bindView:
                break;
        }
    }
}
