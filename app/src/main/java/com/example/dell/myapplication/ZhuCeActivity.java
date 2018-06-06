package com.example.dell.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ZhuCeActivity extends AppCompatActivity implements ImageView.OnClickListener{
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhucelayout);
        init();
    }

    private void init() {
        imageView = findViewById(R.id.zhuce_haihui_denglu);
        imageView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhuce_haihui_denglu:
                Intent intent = new Intent(ZhuCeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
