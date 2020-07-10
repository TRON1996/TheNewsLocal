package com.hzz.thenewslocal.ui.activity;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.MD5Utils;

import org.apache.hc.client5.http.classic.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView registered;
    private TextView forgetPwd;
    private User user = new User();
    Gson gson = new Gson();
    private int LOGIN_SUCCESS = 1;
    private String rootUrl = "http://172.20.10.3:8088/TheNewsWeb_war_exploded/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    private void init() {

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        registered = (TextView) findViewById(R.id.registered);
        registered.setOnClickListener(this);
    forgetPwd=findViewById(R.id.forgetpwd);
        forgetPwd.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                logindataup();
                break;
            case R.id.registered:
                register();

            case R.id.forgetpwd:
                forgetpwd();
        }
    }

    private void forgetpwd() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, ForgetpwdActivity.class);
        startActivity(intent);

    }

    private void register() {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void logindataup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                user.setName(username.getText().toString());
                user.setPassword(MD5Utils.MD5Encode(password.getText().toString()));
                String strUser = gson.toJson(user);
                Map<String, Object> map = new HashMap<>();
                map.put("strUser", strUser);
                try {
                    HttpClientUtils.HttpClientPost(rootUrl + "loginUser", map);
                    Message message = new Message();
                    message.obj = login;
                    handler.sendMessage(message);
                    message.what = LOGIN_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOGIN_SUCCESS) {
                String str = msg.obj.toString();
                Log.i("str", str);
                if (str.equals("error")) {
                    Toast.makeText(LoginActivity.this, "错误", Toast.LENGTH_SHORT).show();
                } else {
                    //成功后执行的代码
                    Log.i("AAAAA", "登录成功");
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

}

