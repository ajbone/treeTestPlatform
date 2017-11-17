package com.dashu.datashow.enums;

/**
 * Created by adrian on 16/8/25.
 */
public enum bizEnums {
    dev("1","运维"),paas("2","PaaS"),test("3","测试"),platform("4","内部平台"),message("5","消息"),front("6","前端"),mobile("7","移动"),ump("8","UMP"),db("9","数据"),youli("10","有你"),business("11","交易"),pay("12","支付"),network("13","网关"),account("14","账户"),goods("15","商品"),distribution("16","分销"),seller("17","销售员"),crm("18","CRM"),door("19","入口"),shop("20","店铺"),app("21","应用"),wechat("22","微信"),pd("23","产品"),customerSat("24","客满"),customerSer("25","客户服务"),other("26","其他");

    String code;
    String desc;

    private bizEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

}


