package com.hzz.thenewslocal.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.PublicString;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView registered;
    private TextView forgetPwd;
    public SharedPreferences sharedPreferences;

    Gson gson = new Gson();
    private int LOGIN_SUCCESS = 1;


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
        forgetPwd = findViewById(R.id.forgetpwd);
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
                break;
            case R.id.forgetpwd:
                forgetpwd();
                break;
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
                User user = new User();
                user.setName(username.getText().toString());
                /*  user.setPassword(MD5Utils.MD5Encode(password.getText().toString()));*/
                user.setPassword(password.getText().toString());
                String strUser = gson.toJson(user);
                Map<String, Object> map = new HashMap<>();
                map.put("strUser", strUser);
                try {
                    String backstr = HttpClientUtils.HttpClientPost(PublicString.rootUrl + "loginUser", map);
                    Message message = new Message();

                    message.obj = backstr;
                    message.what = LOGIN_SUCCESS;
                    handler.sendMessage(message);

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
                if (str == "") {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    //成功后执行的代码
                    Log.i("AAAA", "用户登录成功");
                    LoadUserData(str);
                    LoadDataDemo();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

    private void LoadUserData(String str) {
        User loudUser = new User();
        loudUser = gson.fromJson(str, User.class);
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", loudUser.getId());
        editor.putString("name", loudUser.getName());
        editor.putString("sex", loudUser.getSex());
        editor.putString("phone", loudUser.getPhone());
        editor.putString("password", loudUser.getPassword());
        editor.putString("photo", loudUser.getPhoto());
        editor.putString("loginImg", loudUser.getLoginImg());
        editor.commit();

    }

// 测试sp取数据
    public void LoadDataDemo() {
        SharedPreferences sp=getSharedPreferences("logindata", MODE_PRIVATE);
        String spName = sp.getString("name", "");
        Log.i("AAAA",spName);


    }
}

