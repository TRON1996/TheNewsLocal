package com.hzz.thenewslocal.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.ui.fragment.MeFragment;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.HandleOSImagePath;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.PublicString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText cgName;
    private EditText cgPwd;
    private EditText cgPwd2;
    private EditText cgSex;
    private EditText cgSelfinfo;
    private Button upCgUser;
    private TextView infoBack;
    public SharedPreferences sharedPreferences;
    private ImageView userImgUp;
    private static final int REQUEST_CODE_SYS_IMG = 10;
    protected Map<String, File> loginFileMap = new HashMap<String, File>();
    User user = new User();
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
        userImgUp=findViewById(R.id.userimgup);
        userImgUp.setOnClickListener(this);
        infoBack=findViewById(R.id.infoback);
        infoBack.setOnClickListener(this);


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
            case R.id.userimgup:
                openloginImage();
                break;
            case R.id.infoback:
                inforBack();
                break;

        }
    }

    @SuppressLint("ResourceType")
    private void inforBack() {
        Intent intent = new Intent();
        intent.setClass(UserInfoActivity.this, MainActivity.class);
        intent.putExtra("fragid",1);
        startActivity(intent);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // import android.support.v4.app.FragmentTransaction;

        transaction.replace(R.layout.fragment_me, new MeFragment());
        transaction.commit();
    }

    private void openloginImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_SYS_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SYS_IMG && resultCode == RESULT_OK) {
            //如果调用到REQUEST_CODE_SYS_IMG，则在REQUEST_CODE_SYS_IMG的activity中执行if中的代码
            //获取图片本地路径
            String path = HandleOSImagePath.ImagePath(this, data);
            Log.i("AAAA","huo");
            insertLoginBitmap(path);
            //获取文件后缀比如：.jpg
            String filefix = path.substring(path.lastIndexOf("."));
            //根据电话生成上传文件名
            SharedPreferences spCg = getSharedPreferences("logindata", MODE_PRIVATE);
            String spname= spCg.getString("name", "");

            String imgName = spname;
            String fileName = imgName + filefix;
            //加入文件Map
            user.setLoginImg(fileName);
            loginFileMap.put(fileName, new File(path));
            //插入图片
        }

    }

    private void insertLoginBitmap(String path) {
        SpannableString spannableString = new SpannableString(path);
        int width = userImgUp.getWidth();
        int heigth = userImgUp.getHeight();
        Bitmap zoombmp = BitmapCompress.getSmallBitmap(path, width, heigth, 30);
        ImageSpan imageSpan = new ImageSpan(this, zoombmp);
        spannableString.setSpan(imageSpan, 0, path.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //插入图片
        userImgUp.setImageBitmap(zoombmp);
    }

    private void upCgUsertoweb() {
        Log.i("AAAA", "两次密码一样，开始上传");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences spCg = getSharedPreferences("logindata", MODE_PRIVATE);
                int spInt = spCg.getInt("id", 0);

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
                    LoginimgUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void LoginimgUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences spCg = getSharedPreferences("logindata", MODE_PRIVATE);
                    String spname= spCg.getString("name", "");

                    Map<String, String> umap = new HashMap<>();

                    umap.put("dic", spname);
                    HttpClientUtils.HttpMultipartPost(PublicString.rootUrl + "logingimgup", umap, loginFileMap );
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
