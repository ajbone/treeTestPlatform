package com.dashu.datashow.alert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

//import org.apache.http.HttpEntity;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;

import java.util.List;


/**
 * Created by shenzhaohua on 16/8/4.
 */
public class AlertClient {

//  public static final String ALERT_URL = "http://alert.s.qima-inc.com/api/v1/alert";
//
//  //TODO taskID 和 AppID 自己填一下
//  public static final int ALERT_TASK_ID = 40;
//  public static final String ALERT_APP_ID = "7abf21e3-78b7-40b3-98ea-8339365b5868";
//
////  protected static CloseableHttpClient httpClient;
//
//  /**连接超时时间,单位为毫秒,默认3000**/
//  protected static int connectionTimeout = 3000;
//
//  /**读取数据超时时间,单位为毫秒,默认3000**/
//  protected static int socketTimeout = 3000;
//
////  static{
////    httpClient = HttpClientBuilder.create()
////        .setDefaultRequestConfig(buildRequestConfig(connectionTimeout, socketTimeout)).build();
////  }
//
////  private static RequestConfig buildRequestConfig(int connectionTimeout, int socketTimeout) {
////    return RequestConfig.custom()
////        .setConnectTimeout(connectionTimeout)
////        .setSocketTimeout(socketTimeout)
////        .build();
////  }
//
//  /**
//   * 发送告警信息,默认通道为有人
//   * @param content
//   * @param receiver
//   */
//  public static void send(String content, String receiver) {
//    List<String> channels = Lists.newArrayList(Channel.YOUREN.getName(), Channel.EMAIL.getName());
//    send(content, receiver, channels);
//  }
//
//  /**
//   * 发送告警信息,需要设置通道列表
//   * @param content 告警内容
//   * @param receiver 接受者,名字需要和CAS账号一致
//   * @param channels 通道
//   */
//  public static void send(String content, String receiver, List<String> channels) {
//    AlertRequest alertRequest = new AlertRequest();
//    alertRequest.setTaskId(ALERT_TASK_ID);
//    alertRequest.setAppId(ALERT_APP_ID);
//    alertRequest.setContent(content);
//    alertRequest.setChannel(channels);
//    alertRequest.setReceiver(Lists.newArrayList(receiver));
//    alertRequest.setHost("opsbb-jenkins0");
//    alertRequest.setApp("CI");
//    alertRequest.setLevel("WARN");
//    alertRequest.setExtend(null);
//    doPost(ALERT_URL, alertRequest);
//  }
//
//  private static String doPost(String url, AlertRequest alertRequest) {
//    if (url == null || url.length() == 0) {
//      return null;
//    }
////    HttpPost httpPost = null;
////    try {
////      httpPost = new HttpPost(url);
////      String json = JSON.toJSONString(alertRequest);
////      StringEntity entity = new StringEntity(json, "UTF-8");
////      entity.setContentEncoding("UTF-8");
////      entity.setContentType("application/json");
////      httpPost.setEntity(entity);
////      CloseableHttpResponse response = httpClient.execute(httpPost);
////      int statusCode = response.getStatusLine().getStatusCode();
////      if (statusCode != 200) {
////        httpPost.abort();
////        throw new RuntimeException("HttpClient,error status code :" + statusCode);
////      }
////      HttpEntity responseEntity = response.getEntity();
////      String result = null;
////      if (responseEntity != null) {
////        result = EntityUtils.toString(entity, "UTF-8");
////      } else {
////        throw new ClientProtocolException("Response contains no content");
////      }
////      EntityUtils.consume(responseEntity);
////      response.close();
////      return result;
////    } catch (Exception e) {
////      throw new RuntimeException(e);
////    }
//          return url;
//
//  }
}
