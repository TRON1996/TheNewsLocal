package com.hzz.thenewslocal.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.PublicString;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText cgName;
    private EditText cgPwd;
    private EditText cgPwd2;
    private EditText cgSex;
    private EditText cgSelfinfo;
    private Button upCgUser;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        getlocalUserinfor();//得到本储存的用户信息
    }

    private void getlocalUserinfor() {
        SharedPreferences spCg = getSharedPreferences("logindata", MODE_PRIVATE);
        int spInt = spCg.getInt("id", 0);
        String spName = spCg.getString("name", "");
        String spsex = spCg.getString("sex", "");
        String spphone = spCg.getString("phone", "");
        String sppassword = spCg.getString("password", "");
        String sploginImg = spCg.getString("loginImg", "");
        Log.i("AAAA", "取出本地信息" + spName + spsex + spphone + sppassword + sploginImg);
        cgName = findViewById(R.id.cgname);
        cgName.setText(spName);
        cgPwd = findViewById(R.id.cgpwd);
        cgPwd2 = findViewById(R.id.cgpwd2);
        cgSex = findViewById(R.id.cgsex);
        cgSelfinfo = findViewById(R.id.cgselfinfo);
        upCgUser = findViewById(R.id.upcgUser);
        upCgUser.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upcgUser:
                if (cgPwd.getText().toString().trim().equals(cgPwd2.getText().toString().trim())) {
                    upCgUsertoweb();//上传修改的个人信息到服务器
                    upCgUsertoload();//保存修改的个人信息到本地
                }
                break;
        }
    }

    private void upCgUsertoweb() {
        Log.i("AAAA", "两次密码一样，开始上传");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences spCg = getSharedPreferences("logindata", MODE_PRIVATE);
                int spInt = spCg.getInt("id", 0);
                User user = new User();
                user.setId(spInt);
                user.setName(cgName.getText().toString());
                user.setPassword(cgPwd.getText().toString());
                user.setSex(cgSex.getText().toString());
                user.setSex(cgSex.getText().toString());
                user.setSelfinfo(cgSelfinfo.getText().toString());
                Gson gson = new Gson();
                String upDataUserInfo = gson.toJson(user);

                Map<String, Object> map = new HashMap<>();
                map.put("updateuserinfo", upDataUserInfo);
                Log.i("AAA", upDataUserInfo);
                try {

                    HttpClientUtils.HttpClientPost(PublicString.rootUrl + "updateuserinfo", map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void upCgUsertoload() {
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", cgName.getText().toString());
        editor.putString("sex", cgSex.getText().toString());
        editor.putString("password", cgPwd.getText().toString());
        editor.putString("selfinfo", cgSelfinfo.getText().toString());
        editor.commit();
    }


}
