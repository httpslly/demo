package com.example.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class DemoHttpClient {
  String path = "https://www.baidu.com";
  String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36";

  /**
   * 原生
   * 最简
   * @throws IOException
   */
  @Test
  public void test1() throws IOException {
    URL url = new URL(path);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    InputStream inputStream = connection.getInputStream();

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


    String line = reader.readLine();
    while (line != null) {
      System.out.println(line);
      line = reader.readLine();
    }

    reader.close();
    inputStream.close();
  }

  /**
   * 原生
   * get、post，请求头
   * @throws IOException
   */
  @Test
  public void test2() throws IOException {
    URL url = new URL(path);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    //设置请求头
    //GET、POST必须大写
    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", userAgent);

    InputStream inputStream = connection.getInputStream();

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String str = reader.readLine();

    while (str != null) {
      System.out.println(str);
      str = reader.readLine();
    }

    reader.close();
    inputStream.close();

  }

  /**
   * 原生
   * 参数
   * @throws IOException
   */
  @Test
  public void test3() throws IOException {
    URL url = new URL(path);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", userAgent);

    //通过输出流发送参数
    connection.setDoOutput(true);
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write("username=xiaomi&password=123456".getBytes());

    //获取网页的字节流数据
    InputStream inputStream = connection.getInputStream();

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String str = reader.readLine();

    while (str != null) {
      System.out.println(str);
      str = reader.readLine();
    }

    reader.close();
    inputStream.close();
    outputStream.close();
  }

  /**
   * httpClient
   * 简单
   */
  @Test
  public void httpClientTest1() throws IOException {
    //1 创建httpClient对象
    CloseableHttpClient httpClient = HttpClients.createDefault();
    //1 创建 post、get对象
    HttpGet httpGet = new HttpGet(path);
    httpGet.setHeader("User-Agent", userAgent);
    CloseableHttpResponse response = httpClient.execute(httpGet);

    HttpEntity entity = response.getEntity();
    InputStream inputStream = entity.getContent();

    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String str = reader.readLine();

    while (str != null) {
      System.out.println(str);
      str = reader.readLine();
    }

    reader.close();
    inputStream.close();
    response.close();

  }

  /**
   * httpClient
   * 简单，使用EntityUtils
   * @throws IOException
   */
  @Test
  public void httpClientTest2() throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet get = new HttpGet(path);

    //不添加消息头，百度只返回html
    //get.setHeader("User-Agent", userAgent);
    get.setHeader("User-Agent", userAgent);
    CloseableHttpResponse response = httpClient.execute(get);
    HttpEntity entity = response.getEntity();



    String string = EntityUtils.toString(entity, "UTF-8");
    System.out.println(string);
    response.close();
  }

  /**
   * httpClient
   * 传参
   * @throws IOException
   */
  @Test
  public void httpClientTest3() throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(path);
    httpPost.setHeader("User-Agent", userAgent);

    //设置请求体信息
    List<NameValuePair> list = new ArrayList<>();
    list.add(new BasicNameValuePair("name", "zhangsan"));
    list.add(new BasicNameValuePair("age", "20"));
    httpPost.setEntity(new UrlEncodedFormEntity(list));

    CloseableHttpResponse response = httpClient.execute(httpPost);
    HttpEntity entity = response.getEntity();

    System.out.println(EntityUtils.toString(entity, "UTF-8"));

    response.close();

  }
}
