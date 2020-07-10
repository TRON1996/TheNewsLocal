package com.hzz.thenewslocal.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.HandleOSImagePath;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.MD5Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SYS_IMG = 10;
    private int RESULT_LOAD_IMG = 1;
    private EditText reusername;
    private EditText repassword;
    private Button registerup;
    private EditText phonenum;
    private ImageView loginImg;
    private String rootUrl = "http://172.20.10.3:8088/TheNewsWeb_war_exploded/";
    private String imgPath;
    private ProgressDialog prgDialog;
    private Bitmap bitmap;
    private String encodedString;
    private EditText editTextName;
    private Editable logintable;
    public User userp = new User();
    protected Map<String, File> loginFileMap = new HashMap<String, File>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        init();
     /*  phoneNumget();*/
       /* List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(RegisterActivity.this, permissions, 1);

        }*/
    }

    private void init() {
        reusername = (EditText) findViewById(R.id.reusername);
        repassword = (EditText) findViewById(R.id.repassword);
        phonenum = (EditText) findViewById(R.id.phonenum);
        registerup = (Button) findViewById(R.id.registerup);
       registerup.setOnClickListener(this);
        loginImg = (ImageView) findViewById(R.id.loginimg);
        loginImg.setOnClickListener(this);
    }

   /*private void phoneNumget() {
      *//*  TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        }*//*
       phonenum.setText("1111111111");
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginimg:
           /*     openloginImage();*/
                break;
            case R.id.registerup:
                userdataup();
        }


    }

    /*private void openloginImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_SYS_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SYS_IMG && resultCode == RESULT_OK) {//如果调用到REQUEST_CODE_SYS_IMG，则在REQUEST_CODE_SYS_IMG的activity中执行if中的代码
            //获取图片本地路径

            String path = HandleOSImagePath.ImagePath(this, data);
            insertBitmap(path);
            //获取文件后缀比如：.jpg
            String filefix = path.substring(path.lastIndexOf("."));
            //根据电话生成上传文件名

            String imgName = phonenum.getText().toString();
            String fileName = imgName + filefix;
            //加入文件Map
            userp.setLoginImg(fileName);
            loginFileMap.put(fileName, new File(path));
            //插入图片



        }
    }

    //插入图片,在头像框中显示图片
    private void insertBitmap(String path) {
        SpannableString spannableString = new SpannableString(path);
        int width = loginImg.getWidth();
        int heigth = loginImg.getHeight();
        Bitmap zoombmp = BitmapCompress.getSmallBitmap(path, width, heigth, 30);
        ImageSpan imageSpan = new ImageSpan(this, zoombmp);
        spannableString.setSpan(imageSpan, 0, path.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //插入图片
        loginImg.setImageBitmap(zoombmp);
    }
*/
    //个人信息上传
    private void userdataup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = reusername.getText().toString();
                String password = MD5Utils.MD5Encode(repassword.getText().toString());
                String phone = phonenum.getText().toString();
                userp.setName(username);
                userp.setPassword(password);
               /* userp.setPhone(phone);*/
                Gson gson = new Gson();
                String strUser = gson.toJson(userp);
                Map<String, Object> map = new HashMap<>();
                map.put("strUser", strUser);
                Log.i("AAA", strUser);
                try {
                    HttpClientUtils.HttpClientPost(rootUrl + "userAction", map);
              /*      publishuserImg();*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //头像文件上传
    private void publishuserImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> umap = new HashMap<>();
                    String phone = phonenum.getText().toString();
                    umap.put("dic", phone);
                    HttpClientUtils.HttpMultipartPost(rootUrl + "userImgAction", umap, loginFileMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
