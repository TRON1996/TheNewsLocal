package com.hzz.thenewslocal.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowNewDetailActivity extends AppCompatActivity {
    private String rooturl="http://192.168.0.115:8080/com.zhhf.Frommediadem/img/";
    private String loadcontent="啊撒地方士大夫撒地方士大夫士大夫上的，撒地方撒发撒阿飞士大夫阿斯发生大是否撒发是。" +
            "    <img src=timg2.jpeg/>\n" +
            "    啊大是否撒发是否撒发发生电风扇打发说法士大夫撒发是否撒地方撒地方撒打发是否是撒旦发生的发生打发是否撒打发但是发生发是否。\n" +
            "    <img src=timg.jpeg/>";
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
    }

    private void loadNews(String txt) {
        String imgTag="<img src=([\\w/.]*)/>";
        Pattern pattern=Pattern.compile(imgTag);
        Matcher matcher= pattern.matcher(txt);
        while (matcher.find()) {
            final String tag = matcher.group();
            String imgName = matcher.group(1);
            final int start = txt.indexOf(tag);
            final int end = start + tag.length();
            String url=rooturl+imgName;
            imageLoader.loadImage(rooturl+imgName,ImageLoaderUtil.getOptions(),new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Log.i("AAA","aaaaa");
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    replaceImage(loadedImage,start,end,tag);
                }
            });
        }

    }

    private void replaceImage(Bitmap loadedImage, int start, int end,String imgTag) {

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
