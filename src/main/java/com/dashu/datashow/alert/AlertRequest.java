package com.dashu.datashow.alert;

import java.util.List;

/**
 * Created by shenzhaohua on 16/8/5.
 */
public class AlertRequest {

  public int taskId;
  public String appId;
  public String content;
  public List<String> channel;
  public List<String> receiver;
  public String host;
  public String app;
  public String level;
  public String extend;

  public int getTaskId() {
    return taskId;
  }

  public void setTaskId(int taskId) {
    this.taskId = taskId;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getChannel() {
    return channel;
  }

  public void setChannel(List<String> channel) {
    this.channel = channel;
  }

  public List<String> getReceiver() {
    return receiver;
  }

  public void setReceiver(List<String> receiver) {
    this.receiver = receiver;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getExtend() {
    return extend;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }

}
