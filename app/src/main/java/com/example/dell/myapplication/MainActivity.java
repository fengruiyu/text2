package com.example.dell.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.solver.GoalRow;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{
    private static final String TAG = "MainActivity";
    private Button button1,button2;
    private TextView shouji,mima;
    private MyHandler myHandler = new MyHandler();
    //注释
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initclick();
    }

    private void initclick() {
        button1.setOnClickListener(this);
       button2.setOnClickListener(this);
    }
    private void init() {
        button1 = findViewById(R.id.button_denglu);
        button2 = findViewById(R.id.button_zhuce);
        shouji = findViewById(R.id.main_shouji);
        mima = findViewById(R.id.main_mima);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_zhuce :
                Intent intent = new Intent(MainActivity.this,ZhuCeActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: ");
                break;
            case R.id.button_denglu :
                Log.d(TAG, "onClick: ");
                final String s= (String) shouji.getText().toString();
                String m= (String) mima.getText().toString();
                boolean a1 =false;
                boolean a2 =false;
                if (checkPhoneNumber(s)){
                    //Toast.makeText(MainActivity.this,"手机号正确",Toast.LENGTH_LONG).show();
                    a1=true;
                }else {
                    Toast.makeText(MainActivity.this,"手机号不对",Toast.LENGTH_LONG).show();
                }
                if (m.length()>=6){
                    //Toast.makeText(MainActivity.this,"密码正确",Toast.LENGTH_LONG).show();
                    a2=true;
                }else {
                    Toast.makeText(MainActivity.this,"密码不对",Toast.LENGTH_LONG).show();
                }
                if (a1&&a2){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                URL url = new URL("http://120.27.23.105/user/login?mobile="+shouji.getText().toString()+"&password="+mima.getText().toString());
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setConnectTimeout(5000);
                                if (httpURLConnection.getResponseCode()==200){
                                    InputStream inputStream = httpURLConnection.getInputStream();
                                    String s1 = streamTostring(inputStream);
                                    Gson gson = new Gson();
                                    Bean bean = gson.fromJson(s1, Bean.class);
                                    String code = bean.getCode();
//                                    Log.d(TAG, "run: "+gson);
//                                    Log.d(TAG, "run: "+code);
                                    if (code.equals("0")){
                                        Message message = myHandler.obtainMessage();
                                        message.what=0;
                                        myHandler.sendMessage(message);
                                    }else {
                                        Message message = myHandler.obtainMessage();
                                        message.what=1;
                                        myHandler.sendMessage(message);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
        }
    }
    public static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern=Pattern.compile("^1[0-9]{10}$");
        Matcher matcher=pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    private String streamTostring(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((str=bufferedReader.readLine())!=null){
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  stringBuilder.toString();

    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_LONG).show();
            }
        }
    }
}
//注释
