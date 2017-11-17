package com.dashu.datashow.alert;

/**
 * Created by shenzhaohua on 16/8/5.
 */
public enum Channel {

  SMS("sms", "短信"),
  EMAIL("email", "邮件"),
  YOUREN("youren", "有人APP");

  String name;
  String desc;

  Channel(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
