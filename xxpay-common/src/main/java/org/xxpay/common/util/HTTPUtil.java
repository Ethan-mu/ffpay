package org.xxpay.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 发送http post请求，携带json参数
 */
public class HTTPUtil {

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=18_N9sSkWY2VEru2cZIhKUT6R257KwZoH6YUqbcb5aeJ2IHzzgAJYUoWSGiWd7elruz6KwbX3b1PSxjUOgYT652qZ0HQVa62z-W9saejsLN7ooqBUDVTPrrmwgbNAUGNXeAGASZJ";
        //Post方式提交Json字符串，按照指定币种，指定时间点查询
        String json1 = "{\"touser\":\"o0kX6jkWlQdD6uX8qPQrH_X3gH44\",\"template_id\":\"xpE3b6m6yfUImJClj0hWh-RvakixC3kD7OhEeDltxzM\",\"url\":\"http://weixin.qq.com/download\",\"data\":{}}";
        //Post方式提交json字符串，按照指定的币种和时间正序的方式获取前N条数据
        String json2 = "{\"query\":{\"match\":{\"imtype\":\"LTCUS\"}},\"sort\":[{\"rtdatetime\":{\"order\":\"desc\"}}],\"size\":3}";
        //Post方式提交Json字符串，按照指定币种，指定时间段查询
        String json3 = "{\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"imtype\":\"LTCUS\"}},{\"range\":{\"rtdatetime\":{\"gte\":1521164922000,\"lte\":1521164932000}}}]}}}";

//        String json4={"touser":"o0kX6jkWlQdD6uX8qPQrH_X3gH44","data":"","template_id":"xpE3b6m6yfUImJClj0hWh-RvakixC3kD7OhEeDltxzM","url":"http://weixin.qq.com/download"};

        String json = json1;

        System.out.println(HTTPUtil.HttpPostWithJson(url, json));

    }

    public static String HttpPostWithJson(String url, String json) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }

    /**
     * 发送get请求不带参数
     * @param requestUrl
     * @return
     */
    public static String sendGet(String requestUrl) {
        StringBuffer buffer = null;

        try {
            // 建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            // 获取输入流
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 读取返回结果
            buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}