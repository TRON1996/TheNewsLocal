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
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.ImageLoaderUtil;
import com.hzz.thenewslocal.utils.PublicString;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowNewDetailActivity extends AppCompatActivity {
private TextView detailTitle;
private TextView    pushUser;



    private String loadcontent="aaa";

    private EditText etShow;
    private Editable editable;
    private ImageLoader imageLoader= ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_new_detail);
        ImageLoaderUtil.ImageLoaderInit(this);
        initView();

        loadNews(loadcontent);
    }

    private void initView() {
        etShow=findViewById(R.id.etShow);
        etShow.setText(loadcontent);
        detailTitle =findViewById(R.id.detailtitle);
        pushUser=findViewById(R.id.pushuser);
        //取出intent传过来的数据
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        String title=intent.getStringExtra("title");
        String time=intent.getStringExtra("time");
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String id=intent.getStringExtra("id");
        Log.i("CCCC","新闻详细也intend传过来的值："+content+title+time+phone+name);
        etShow=findViewById(R.id.etShow);
        etShow.setText(content);
        detailTitle.setText(title);
        pushUser.setText(name);
        loadNews(content);
    }

    private void loadNews(String txt) {
        String imgTag="<img src=([\\w/.]*)/>";
        Intent intent=getIntent();
        String phone=intent.getStringExtra("phone");
        Pattern pattern=Pattern.compile(imgTag);
        Matcher matcher= pattern.matcher(txt);
        while (matcher.find()){
            final String tag=matcher.group();
            String strimagename=matcher.group(1);
            final int start=txt.indexOf(tag);
            final int end=start+tag.length();
            String url= PublicString.rootUrl+phone+"/"+strimagename;
            imageLoader.loadImage(url,ImageLoaderUtil.getOptions(),new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Log.i("AAA","aaaaa");
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    replaceImage(loadedImage,start,end,tag);
                }
            });
        }
    }

    private void replaceImage(Bitmap loadedImage, int start, int end, String imgTag) {
        Log.i("AAA",start+"");
        Bitmap newbitmap= BitmapCompress.zoomImage(loadedImage,etShow.getWidth());
        ImageSpan span=new ImageSpan(this,newbitmap);
        SpannableString spannableString=new SpannableString(imgTag);
        spannableString.setSpan(span,0,imgTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable=etShow.getText();
        editable.replace(start,end,spannableString);
        etShow.setText(editable);
    }

}
