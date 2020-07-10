package com.hzz.thenewslocal.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.adapter.HomeNewsAdapter;
import com.hzz.thenewslocal.model.News;
import com.hzz.thenewslocal.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ListView lvHomeNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        /////模拟数据
        lvHomeNews=view.findViewById(R.id.lvHomeNews);
        List<News> list=new ArrayList<>();
        News news=new News();
        news.setId(1);
        news.setTitle("这是一个没有图片的非常重要的惊天动地的震撼人心的特别特别大的新闻");
        news.setTime("2020-5-18 15:57");
        List<String> pnames3=new ArrayList<>();
        news.setImgName(pnames3);
        User user=new User();
        user.setName("央视网络");
        news.setUser(user);
        list.add(news);


        News news1=new News();
        news1.setId(1);
        news1.setTitle("这是一个没有图片的非常重要的惊天动地的震撼人心的特别特别大的新闻");
        news1.setTime("2020-5-18 15:57");
        news1.setUser(user);
        List<String> pnames=new ArrayList<>();
        pnames.add("timg.jpeg");
        pnames.add("temp.jpg");
        news1.setImgName(pnames);
        list.add(news1);

        News news2=new News();
        news2.setId(1);
        news2.setTitle("这是一个没有图片的非常重要的惊天动地的震撼人心的特别特别大的新闻");
        news2.setTime("2020-5-18 15:57");
        news2.setUser(user);
        List<String> pnames1=new ArrayList<>();
        pnames1.add("timg.jpeg");
        pnames1.add("temp.jpg");
        pnames1.add("timg2.jpeg");
        news2.setImgName(pnames1);
        list.add(news2);
        //////////////////////////////
        Gson gson=new Gson();
        String json=gson.toJson(list);
        Log.i("AAA",json);
        List<News> nlist=getEntitysFromJson(json);
        HomeNewsAdapter homeNewsAdapter=new HomeNewsAdapter(this.getActivity(),list);
        lvHomeNews.setAdapter(homeNewsAdapter);
        return view;
    }
    private List<News> getEntitysFromJson(String json){

        Gson gson=new Gson();
        List<News> list=gson.fromJson(json,new TypeToken<List<News>>(){}.getType());
        return list;
    }
}
