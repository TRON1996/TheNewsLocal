package com.hzz.thenewslocal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.hzz.thenewslocal.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ImageLoader
 * 顾东虎
 * 2020-4-26
 */
public class ImageLoaderUtil {
    /**
     * ImageLoader初始化配置
     * @param context 设备上下文
     */
    public static void ImageLoaderInit(Context context){
        DisplayImageOptions options = getOptions();
        String path="/storage/emulated/0/DCIM/";
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)// 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(new File(path)))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    /**
     * 获取ImageLoader图片显示属性
     * @return
     */
    public static DisplayImageOptions getOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.displayer(new RoundedVignetteBitmapDisplayer(20,20))//
                //.displayer(new RoundedBitmapDisplayer(20))// 圆角图片
                //.displayer(new CircleBitmapDisplayer(Color.WHITE,5))//圆形
                .displayer(new FadeInBitmapDisplayer(700))//正常图片
                .cacheInMemory(true) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(R.drawable.ic_launcher_background)
                .showImageOnFail(R.drawable.ic_launcher_background)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    /**
     * 获取图片加载事件
     * @return 图片加载事件
     */
    public static SimpleImageLoadingListener getLoadingListener(){
        SimpleImageLoadingListener simpleImageLoadingListener=new SimpleImageLoadingListener() {
            final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    // 是否第一次显示
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        // 图片淡入效果
                        FadeInBitmapDisplayer.animate(imageView, 2000);
                        displayedImages.add(imageUri);
                    }
                }
            }
        };
        return simpleImageLoadingListener;
    }
}
