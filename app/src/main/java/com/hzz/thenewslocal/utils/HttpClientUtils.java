package com.hzz.thenewslocal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    /***
     * 基于HttpClient的GET请求
     * @param getUrl 请求地址
     * @param map  请求参数
     * @return  回传字符串
     * @throws Exception
     */
    public static String HttpClientGet(String getUrl, Map<String,Object> map) throws Exception {
        String backStr="";
        CloseableHttpClient client= HttpClients.createDefault();
        URIBuilder builder=new URIBuilder(getUrl);
        if(map!=null) {
            //通过迭代Map的键值对，构建带有参数的URI
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> pramas = (Map.Entry<String, Object>) iterator.next();
                builder.addParameter(pramas.getKey(), pramas.getValue().toString());
            }
        }
        URI uri=builder.build();
        //将带参数的URI设置为Http Get请求的地址
        HttpGet get=new HttpGet(uri);
        //Http客户端执行GET请求，即向服务器发出Get请求，返回服务器的响应对象response。
        CloseableHttpResponse response= client.execute(get);
        if(response.getCode()== HttpStatus.SC_SUCCESS){
            HttpEntity entity= response.getEntity();
            backStr= EntityUtils.toString(entity,"utf-8");
            EntityUtils.consume(entity);
        }
        return backStr;
    }
    /***
     * 基于HttpClient的Post请求
     * @param getUrl 请求地址
     * @param map  请求参数
     * @return  回传字符串
     * @throws Exception
     */
    public static String HttpClientPost(String getUrl, Map<String,Object> map) throws Exception {
        String backStr="";
        CloseableHttpClient client= HttpClients.createDefault();

        HttpPost post=new HttpPost(getUrl);
        //Post请求绑定参数
        if(map!=null) {
            List<NameValuePair> list=new ArrayList<>();
            //通过迭代Map的键值对，构建带有参数的URI
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> pramas = (Map.Entry<String, Object>) iterator.next();
                NameValuePair pair=new BasicNameValuePair(pramas.getKey(),pramas.getValue().toString());
                list.add(pair);
            }
            UrlEncodedFormEntity encodedFormEntity=new UrlEncodedFormEntity(list,Charset.forName("utf-8"));
            post.setEntity(encodedFormEntity);
        }
        //Http客户端执行Post请求，即向服务器发出Post请求，返回服务器的响应对象response。
        CloseableHttpResponse response= client.execute(post);
        if(response.getCode()== HttpStatus.SC_SUCCESS){
            HttpEntity entity= response.getEntity();
            backStr= EntityUtils.toString(entity,"utf-8");
            EntityUtils.consume(entity);
        }
        return backStr;
    }
    /***
     * 基于HttpClient的GET请求位图
     * @param getUrl 请求地址
     * @param map  请求参数
     * @return  回传位图
     * @throws Exception
     */
    public static Bitmap HttpClientGetBitmap(String getUrl, Map<String,Object> map) throws Exception {
        Bitmap bitmap=null;
        CloseableHttpClient client= HttpClients.createDefault();
        URIBuilder builder=new URIBuilder(getUrl);
        if(map!=null) {
            //通过迭代Map的键值对，构建带有参数的URI
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> pramas = (Map.Entry<String, Object>) iterator.next();
                builder.addParameter(pramas.getKey(), pramas.getValue().toString());
            }
        }
        URI uri=builder.build();
        //将带参数的URI设置为Http Get请求的地址
        HttpGet get=new HttpGet(uri);
        //Http客户端执行GET请求，即向服务器发出Get请求，返回服务器的响应对象response。
        CloseableHttpResponse response= client.execute(get);
        if(response.getCode()== HttpStatus.SC_SUCCESS){
            HttpEntity entity= response.getEntity();
            InputStream is=entity.getContent();
            bitmap= BitmapFactory.decodeStream(is);
            EntityUtils.consume(entity);
        }
        return bitmap;
    }

    /**
     * Multipart表单Post提交
     * @param uploadUrl 提交地址
     * @param fileMap 上传文件MAP
     * @param map Multipart表单中的String属性MAP
     * @return 服务器响应值
     * @throws Exception
     */
    public static String HttpMultipartPost(String uploadUrl,Map<String,String> stringMap, Map<String,File> fileMap) throws Exception {
        String backStr="";
        CloseableHttpClient client= HttpClients.createDefault();
        HttpPost post=new HttpPost(uploadUrl);
        //构建MultipartEntity
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        if(stringMap!=null) {
            Iterator iterator=stringMap.entrySet().iterator();
            //解决中文乱码
            ContentType contentType= ContentType.create("text/plain",Charset.forName("UTF-8"));
            while (iterator.hasNext()){
                //HttpClient文本体
                Map.Entry<String,String> parmas= (Map.Entry<String, String>) iterator.next();
                //设置解决中文乱码的ContentType
                StringBody stringBody=new StringBody(parmas.getValue(),contentType);
                builder.addPart(parmas.getKey(),stringBody);
            }
        }
        //HttpClient文件体
        if(fileMap!=null){
            Iterator iterator=fileMap.entrySet().iterator();
            while (iterator.hasNext()){
                //HttpClient文本体
                Map.Entry<String,File> parmas= (Map.Entry<String, File>) iterator.next();
                FileBody fileBody = new FileBody(parmas.getValue());
                builder.addPart(parmas.getKey(),fileBody);
            }
        }


        HttpEntity multipartEntity=builder.build();
        post.setEntity(multipartEntity);

        //Http客户端执行Post请求，即向服务器发出Post请求，返回服务器的响应对象response。
        CloseableHttpResponse response= client.execute(post);
        if(response.getCode()== HttpStatus.SC_SUCCESS){
            HttpEntity entity= response.getEntity();
            backStr= EntityUtils.toString(entity,"utf-8");
            EntityUtils.consume(entity);
        }
        return backStr;
    }

}
