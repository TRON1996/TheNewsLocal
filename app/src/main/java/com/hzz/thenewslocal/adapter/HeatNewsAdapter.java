package com.hzz.thenewslocal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.model.News;
import com.hzz.thenewslocal.utils.ImageLoaderUtil;
import com.hzz.thenewslocal.utils.PublicString;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HeatNewsAdapter extends BaseAdapter {
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private Context context;
    private List<News> list=new ArrayList<>() ;


    public HeatNewsAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
        ImageLoaderUtil.ImageLoaderInit(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        News news = list.get(position);
        Gson gson = new Gson();
        List<String> list = gson.fromJson(news.getStringimg(), new TypeToken<List<String>>() {
        }.getType());
        Log.i("AAAA","从Gson中获取图片的list："+list);
        news.setImgName((ArrayList<String>)list);
            //将获取到的图片名字的list设置到imgName<String>集合中
        if(news.getImgName() == null || news.getImgName().size()<=0) {
            view = LayoutInflater.from(context).inflate(R.layout.news_type_noimg_item, null);
            TextView tvTitle=view.findViewById(R.id.tvNewTitle);
            TextView tvUserName=view.findViewById(R.id.tvUserName);
            TextView tvTime=view.findViewById(R.id.tvPublishTime);
            tvTitle.setText(news.getTitle());
            tvUserName.setText(news.getUser().getName());
            tvTime.setText(news.getTime());
        }else if(news.getImgName().size()<3){
            view = LayoutInflater.from(context).inflate(R.layout.news_type_img1_item, null);
            TextView tvTitle=view.findViewById(R.id.tvNewTitle);
            TextView tvUserName=view.findViewById(R.id.tvUserName);
            TextView tvTime=view.findViewById(R.id.tvPublishTime);
            ImageView iv=view.findViewById(R.id.imageView);
            tvTitle.setText(news.getTitle());
            tvUserName.setText(news.getUser().getName());
            tvTime.setText(news.getTime());

            imageLoader.displayImage(PublicString.rootUrl+"/"+"18589339339"+"/"+news.getImgName().get(0),iv);
            Log.i("AAAA","图片请求地址："+PublicString.rootUrl+"/"+"18589339339"+"/"+news.getImgName().get(0));
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.news_type_img3_item, null);
            TextView tvTitle=view.findViewById(R.id.tvNewTitle);
            TextView tvUserName=view.findViewById(R.id.tvUserName);
            TextView tvTime=view.findViewById(R.id.tvPublishTime);
            ImageView iv1=view.findViewById(R.id.imageView2);
            ImageView iv2=view.findViewById(R.id.imageView3);
            ImageView iv3=view.findViewById(R.id.imageView4);

            tvTitle.setText(news.getTitle());
            tvUserName.setText(news.getUser().getName());
            tvTime.setText(news.getTime());
            imageLoader.displayImage(PublicString.rootUrl+"/"+"18589339339"+"/"+news.getImgName().get(0),iv1);
            imageLoader.displayImage(PublicString.rootUrl+"/"+"18589339339"+"/"+news.getImgName().get(1),iv2);
            imageLoader.displayImage(PublicString.rootUrl+"/"+"18589339339"+"/"+news.getImgName().get(2),iv3);
        }
        return view;
    }
}