package com.hzz.thenewslocal.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.Collect;
import com.hzz.thenewslocal.model.News;
import com.hzz.thenewslocal.model.User;
import com.hzz.thenewslocal.utils.BitmapCompress;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.ImageLoaderUtil;
import com.hzz.thenewslocal.utils.PublicString;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowNewDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView detailTitle;
    private TextView pushUser;
    private String loadcontent = "";
    private EditText etShow;
    private Editable editable;
    private ImageView Collectimg;
    private ImageLoader imageLoader = ImageLoader.getInstance();


    private int newsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_new_detail);
        ImageLoaderUtil.ImageLoaderInit(this);
        initView();
        loadNews(loadcontent);
    }

    private void initView() {
        etShow = findViewById(R.id.etShow);
        etShow.setText(loadcontent);
        detailTitle = findViewById(R.id.detailtitle);
        pushUser = findViewById(R.id.pushuser);
        Collectimg = findViewById(R.id.ivcollect);
        Collectimg.setOnClickListener(this);
        //取出intent传过来的数据
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        newsid = intent.getIntExtra("id", 0);
        /*     int newsID = Integer.parseInt(id);*/
        Log.i("CCCC", "新闻详细也intend传过来的值：" + content + title + time + phone + name + newsid);
        etShow = findViewById(R.id.etShow);
        etShow.setText(content);
        detailTitle.setText(title);
        pushUser.setText(name);
        loadNews(content);
    }

    private void loadNews(String txt) {
        String imgTag = "<img src=([\\w/.]*)/>";
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        Pattern pattern = Pattern.compile(imgTag);
        Matcher matcher = pattern.matcher(txt);
        while (matcher.find()) {
            final String tag = matcher.group();
            String strimagename = matcher.group(1);
            final int start = txt.indexOf(tag);
            final int end = start + tag.length();
            String url = PublicString.rootUrl + phone + "/" + strimagename;
            imageLoader.loadImage(url, ImageLoaderUtil.getOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Log.i("AAA", "aaaaa");
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    replaceImage(loadedImage, start, end, tag);
                }
            });
        }
    }

    private void replaceImage(Bitmap loadedImage, int start, int end, String imgTag) {
        Log.i("AAA", start + "");
        Bitmap newbitmap = BitmapCompress.zoomImage(loadedImage, etShow.getWidth());
        ImageSpan span = new ImageSpan(this, newbitmap);
        SpannableString spannableString = new SpannableString(imgTag);
        spannableString.setSpan(span, 0, imgTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable = etShow.getText();
        editable.replace(start, end, spannableString);
        etShow.setText(editable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivcollect:
                collectUp();
                break;

        }

    }

    private void collectUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Collect collect = new Collect();

                SharedPreferences userID = getSharedPreferences("logindata", MODE_PRIVATE);
                int userId = userID.getInt("id", 0);
                News news = new News();
                User user = new User();

                news.setId(newsid);
                user.setId(userId);
                collect.setNews(news);
                collect.setUser(user);
                Gson gson = new Gson();
                String strCollect = gson.toJson(collect);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("strCollect", strCollect);
                try {
                    HttpClientUtils.HttpClientPost(PublicString.rootUrl + "Collectionup", map);
                    Log.i("收藏：", map.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    /**
     * 弹出评论对话框
     */
    /*private static Dialog showInputComment(Activity activity, CharSequence hint, final CommentDialogListener listener) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.view_input_comment);
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });
        final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
        input.setHint(hint);
        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickPublish(dialog, input, btn);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(coord);
                    // 传入 输入框距离屏幕顶部（不包括状态栏）的长度
                    listener.onShow(coord);
                }
            }
        }, 300);
        return dialog;
    }

    public interface CommentDialogListener {
        void onClickPublish(Dialog dialog, EditText input, TextView btn);

        void onShow(int[] inputViewCoordinatesOnScreen);

        void onDismiss();
    }*/
}
