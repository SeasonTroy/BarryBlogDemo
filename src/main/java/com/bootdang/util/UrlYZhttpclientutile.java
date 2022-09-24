package com.bootdang.util;

import net.sf.ehcache.CacheManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UrlYZhttpclientutile {

/*
* 验证资源连接是否有效*/

    public static Boolean postOpen(String url) throws IOException {
        CloseableHttpClient HTTP_CLIENT=HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
        CloseableHttpResponse execute;
        try {
           execute = HTTP_CLIENT.execute(httpGet);
        }catch(Exception e){
            return false;
        }
        HttpEntity entity = execute.getEntity();
        String s = EntityUtils.toString(entity,"UTF-8");
        System.out.println(s);
        execute.close();
            try {
                httpGet.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }finally {
            HTTP_CLIENT.close();
        }

        if(s.contains("请输入提取码")||s.contains("分享无限制")){
            return true;
        }
        return false;

    }

    public static void main (String[] args) throws IOException {
        Boolean aBoolean = postOpen("https://pan.baidu.com/s/1sGZvU13Vq6aJbJNtzSQ4");
        System.out.println(aBoolean);

    }
}
