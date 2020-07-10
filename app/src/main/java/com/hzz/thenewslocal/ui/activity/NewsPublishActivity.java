package com.hzz.thenewslocal.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.News;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.HandleOSImagePath;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.Permissions;
import com.hzz.thenewslocal.utils.PublicString;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsPublishActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SYS_IMG = 10;
    public static final String TAG = "NEWS";
    private ImageView ivImage;
    private EditText etNewTitle;
    private EditText etText;
    private Editable editable;
    private String txt = "";
    private TextView tvPublish;

    private Map<String, String> rfileMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_new_publish);
        Permissions.verifyStoragePermissions(this);
        initView();
    }

    private void initView() {
        ivImage = findViewById(R.id.ivImage);
        etNewTitle = findViewById(R.id.etNewTitle);
        etText = findViewById(R.id.etText);
        tvPublish = findViewById(R.id.tvPublish);
        ivImage.setOnClickListener(this);
        tvPublish.setOnClickListener(this);
        etText.setText("");
        editable = etText.getText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage:
                openImage();
                break;
            case R.id.tvPublish:
                publishNew();
        }
    }

    //新闻发布
    // 由发布按钮触发该方法
    private void publishNew() {

        publishNewinfo();
        publishNewImg();
    }

    //上传内容
    private void publishNewinfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                News news = new News();
                String title = etNewTitle.getText().toString();
                String content = etText.getText().toString();//获取输入框中的标题和内容，转为String
                news.setContent(content);
                news.setTitle(title);//装载内容到新闻model
                Gson gson = new Gson();//创建Gson对象
                String strNews = gson.toJson(news);//把装着内容的新闻modle转为Gson并放入字符串类型的strNews
                Map<String, Object> map = new HashMap<>();//创建Map类型的集合map
                map.put("strNews", strNews);//把Gson字符串放入map键为strNews
                Log.i("AAA", strNews);
                try {
                    HttpClientUtils.HttpClientPost(PublicString.rootUrl + "NewInfoAction", map);
                    //使用HttpClient工具类，调用其中的Post方式，把map发送到指定url的服务器
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //上传图片
    private void publishNewImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = etText.getText().toString();//获取输入框内容并转为content字符串
                Log.i("AAA", content);
                Map<String, File> fileMap = findUploadImg(content);//调用findUploadImg方法，传入content字符串，将返回值装入fileMap类型为map的集合中
                try {
                    //模拟一个用户
                    User user = new User();
                    user.setPhone("18589339339");
                    Map<String, String> umap = new HashMap<>();
                    umap.put("dic", user.getPhone());
                    //上传图片
                    HttpClientUtils.HttpMultipartPost(PublicString.rootUrl + "multipartAction", umap, fileMap);//filemap:(imgName, new File(path))
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //调用相册(打开相册，选中图片)
    private void openImage() {
        Intent intent = new Intent();
        /*intent是一种运行时绑定机制，他能连接两个不同的组件
        * */
        intent.setType("image/*");//设置要调用的类型为图片
        intent.setAction(Intent.ACTION_GET_CONTENT);//采用隐式intent启动系统组建，自动识别ACTION_GET_CONTENT启动那个activity
        startActivityForResult(intent, REQUEST_CODE_SYS_IMG);
        /*它的使用场景就是：比如从AActivity跳转到BActivity，然后在BActivity中做一系列操作，
        然后在BActivity关闭时候需要把一些数据再回传给AActivity，或者当BActivity关闭后，
        需要让AActivity的界面或者数据发生一些变化，这个时候就需要用到 startActivityForResult。*/
    }

    @Override
    //回调函数
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SYS_IMG && resultCode == RESULT_OK) {//如果调用到REQUEST_CODE_SYS_IMG，则在REQUEST_CODE_SYS_IMG的activity中执行if中的代码
            //获取图片本地路径
            String path = HandleOSImagePath.ImagePath(this, data);
            //获取文件后缀比如：.jpg
            String filefix = path.substring(path.lastIndexOf("."));
            //根据时间生成上传文件名
            String fileName = getFileName() + filefix;
            //加入文件Map
            rfileMap.put(fileName, path);
            //插入图片
            insertBitmap(fileName, path);
        }
    }
    //插入图片,在内容框中显示图片
    private void insertBitmap(String fileName, String path) {
        String bmpTag = "<img src=" + fileName + "/>";
        //构建一个字符的SpannableString
        SpannableString spannableString = new SpannableString(bmpTag);
        //构建ImageSpan
        int width = etText.getWidth();
        int heigth = etText.getHeight();

        Bitmap zoombmp = BitmapCompress.getSmallBitmap(path, width, heigth, 30);

        ImageSpan imageSpan = new ImageSpan(this, zoombmp);
        spannableString.setSpan(imageSpan, 0, bmpTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //插入图片
        editable = etText.getText();
        int start = etText.getSelectionStart();
        start = start > editable.length() ? editable.length() : start;
        editable.insert(start, spannableString);
        etText.setText(editable);
        etText.setSelection(editable.length());

    }

    //从发布内容中检索上传图片
    //本方法为map类型接受参数content（内容中含有图片）
    private Map<String, File> findUploadImg(String content) {
        Map<String, File> fileMap = new HashMap<>();//创建fileMa类型为hashmap的集合
        String imgTag = "<img src=([\\u4E00-\\u9FA5A-Za-z0-9%_/.]*)/>";//创建字符串ImgTag内涵有Img标签
        Pattern pattern = Pattern.compile(imgTag);//传入imgTag中的规则，并进行编译
        /*
      Pattern.compile函数语法 ：：模式.编译（）
      Pattern Pattern.compile(String regex, int flag)
      Pattern.compile函数中两个参数
        1、regex 表示定义的规则
        2、flag 表示设置的参数类型，主要包含以下几种情况：
            （1）Pattern.CASE_INSENSITIVE(?i) 默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。让表达式忽略大小写进行匹配。
            （2）Pattern.COMMENTS(?x) 此种模式下，匹配时会忽略表达式中空格字符(表达式里的空格，tab，回车)。注释从#开始，一直到这行结束。
            （3）Pattern.UNIX_LINES(?d) 此种模式下，只有’\n’才被认作一行的中止，并且与’.’，’^’，以及’$’进行匹配。
            （4）Pattern.MULTILINE(?m) 此种模式下，上箭头和单引号分别匹配一行的开始和结束。此外，’^‘仍然匹配字符串的开始，’’也匹配字符串的结束。默认情况下，这两个表达式仅仅匹配字符串的开始和结束。
            （5）Pattern.DOTALL：此种模式下，表达式’.‘可以匹配任意字符，包括表示一行的结束符。默认情况下，表达式’.'不匹配行的结束符。
        * */
        Matcher matcher = pattern.matcher(content);//Matcher：匹配器；把模式传入配配器
        while (matcher.find()) {//如匹配到字符串中的规则，则执行while中代码
            /*find()方法是部分匹配，是查找输入串中与模式匹配的子串，如果该匹配的串有组还可以使用group()函数。
                matches()是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()。
                */
            String imgName = matcher.group(1);
            //返回给定<img href="Pattern.html#groupname">命名捕获组</img>在之前的匹配操作期间捕获的子序列的起始索引
            String path = rfileMap.get(imgName).toString();
            Log.i("AAA", path);
            fileMap.put(imgName, new File(path));
        }
        return fileMap;
    }

    //根据时间获取获取图片名
    private String getFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
