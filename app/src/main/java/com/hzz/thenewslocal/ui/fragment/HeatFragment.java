package com.hzz.thenewslocal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.adapter.HeatNewsAdapter;
import com.hzz.thenewslocal.adapter.HomeNewsAdapter;
import com.hzz.thenewslocal.model.News;
import com.hzz.thenewslocal.ui.activity.ShowNewDetailActivity;
import com.hzz.thenewslocal.utils.HttpClientUtils;
import com.hzz.thenewslocal.utils.PublicString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeatFragment extends Fragment {
    private static final int NEWS_HEAT_MESSAGE = 1004;
    private static final int HEATNEWS_SHOW_MESSAGE = 1008;
    private static final int NEWS_SHOW = 1008;
    private Handler handler;
    private Handler detailHandler;
    private List<News> list = new ArrayList<>();
    private int newsId;
    private ListView listNews;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listNews = view.findViewById(R.id.lvHomeNews);
        //将fragment_home界面加载到HomeFragment的view

        new Thread(new Runnable() {
            @Override
            public void run() {//创建线程
                Message message = new Message();//创建消息
                try {
                    String str = HttpClientUtils.HttpClientGet(PublicString.rootUrl + "heatselect", null);//向服务器中发出请求
                    Gson gson = new Gson();
                    List<News> list = gson.fromJson(str, new TypeToken<List<News>>() {
                    }.getType());
                    //解析服务器中的数据fremJson(json,类型)，TypeToken解析为Adapter类型
                    Log.i("AAAA", "获取数据库中的全部Newsjson：" + str);
                    message.what = NEWS_HEAT_MESSAGE;//消息标识为NEWS_HOME_MESSAGE
                    message.obj = list;//将list装如mssage
                    handler.sendMessage(message);//发送此message;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);//handle接收message
                if (msg.what == NEWS_HEAT_MESSAGE) {//判断表示
                    View view = getView();
                    list = (List<News>) msg.obj;
                    final HeatNewsAdapter heatNewsAdapter = new HeatNewsAdapter(view.getContext(), list);
                    listNews.setAdapter(heatNewsAdapter);
                }
            }
        };

        /*
         * 以下跳转新闻详情页
         * 1、为条目设置点击时间
         * 2、获取点击的新闻的ID（listNews）
         * 3、通过ID向服务器请求该ID的新闻全部内容
         * 4、通过message将取到的Gson数据从子线程中取出来
         * 5、在detailHandle中将Gson数据解析出来携带跳转到详细页面
         * */

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {//为adapter中的条目设置点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsId = list.get(position).getId();//得到点击条目的ID
                final Map<String, Object> map = new HashMap<String, Object>();

                map.put("newsId", newsId);//创建map将获取的id放入map
                Log.i("新闻详细叶相关", "取到的NewsID" + newsId);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();//创建消息
                        try {
                            String strNew = HttpClientUtils.HttpClientPost(PublicString.rootUrl + "shownewsdetial", map);
                            //通过map中的id向服务端获取这个id的信息，并返回为String的GSon
                            Log.i("新闻详细叶相关", "发送的map：" + map);
                            Log.i("新闻详细叶相关", "返回的str：" + strNew);
                            message.what = HEATNEWS_SHOW_MESSAGE;
                            //创建消息标识
                            message.obj = strNew;//将返回的Gson装入消息obj
                            detailHandler.sendMessage(message);//发送obj
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                detailHandler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == HEATNEWS_SHOW_MESSAGE) {//根据NEWS_SHOW_MESSAGE获取obj
                            String obj = msg.obj.toString();//将obj转为Gson串
                            Gson gson = new Gson();
                            News news = gson.fromJson(obj, News.class);//将obj的Gson转为对应的News实体
                            String id = news.getTitle();
                            String title = news.getTitle();
                            String content = news.getContent();
                            String time = news.getTime();
                            String type = news.getType();
                            String name = news.getUser().getName();
                            String phone = news.getUser().getPhone();
                            Log.i("新文详细叶相关", "分别创建对应字符串取出各个值：" + id + name + title + content + time + type);

                            //携带数据跳转到新闻详情页
                            Intent intent = new Intent(getActivity(), ShowNewDetailActivity.class);

                            intent.putExtra("title", title);
                            intent.putExtra("content", content);
                            intent.putExtra("time", time);
                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);
                            intent.putExtra("id", id);
                            startActivityForResult(intent, NEWS_SHOW);
                        }

                    }
                };
            }
        });
        return view;
    }
}
