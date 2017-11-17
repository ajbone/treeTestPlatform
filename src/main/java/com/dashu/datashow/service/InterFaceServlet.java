package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.RedisUtil;
import com.dashu.datashow.util.SignUtil;
import com.dashu.datashow.util.TimeUtil;
import io.restassured.response.Response;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;


public class InterFaceServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            //设置字符编码
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            //从页面获取参数
            String interfaceId = request.getParameter("interfaceId");
            String loginName = request.getParameter("loginName");
            String interfaceType = request.getParameter("interfaceType");
            String testType = request.getParameter("testType");
            String interfaceName = request.getParameter("interfaceName");
            String loginName_seller = request.getParameter("loginName_seller");
            String password_seller = request.getParameter("password_seller");
            String environmentConfig = "";
            String user_token = "";
            String userId = "";
            String postType ="";
            String captch_code ="";
            HashMap<String,String> interfaceConfig = new HashMap<String,String>();
            HashMap<String,String> sessionConfig = new HashMap<String,String>();
            packageData doPackage = new packageData();

            //数据库选择
            if(interfaceType.equals("dev")){
                environmentConfig ="http://test.";
            }

            if(interfaceType.equals("prod")){
                environmentConfig ="https://www.";
            }


        //取入参的一部分
        String interfaceInfo = doPackage.selectStatus("select config from interface where id =" + interfaceId + "");
        Map mapTypes = JSON.parseObject(interfaceInfo);
        for (Object obj : mapTypes.keySet()){
            if(obj!="") {
                sessionConfig.put(obj.toString(), request.getParameter(obj.toString()).replace("\"", "&quot;"));
            }
        }
        //写入缓存
        request.getSession().setAttribute("ConfigValue", sessionConfig);

            //分user和seller
        if(testType.equals("user")) {
            //取验证码
            Jedis jedis = RedisUtil.getJedis();
            captch_code = jedis.get("USERCENTER:SMS:"+loginName);
            RedisUtil.close(jedis);

            if (captch_code==null) {
                request.getSession().setAttribute("verification", "expired");
                request.getSession().removeAttribute("result");
                response.sendRedirect("/pages/interface.jspa?interfaceId="+interfaceId);
                request.getSession().setAttribute("sessionId", interfaceId);
                return;
            }
            interfaceConfig.put("mobile",loginName);
            interfaceConfig.put("code", captch_code);
            postType =environmentConfig+"91gfd.com.cn/usercenterv2/api/customers/captchaLogin";
        }else if(testType.equals("seller")){
            postType =environmentConfig+"91gfd.com.cn/usercenterv2/api/customers/captchaLogin";
            interfaceConfig.put("device_num","123654");
            interfaceConfig.put("type","8");
            interfaceConfig.put("login_name", loginName_seller);
            interfaceConfig.put("password", password_seller);
        }

            if(!testType.equals("nologin")) {
                //登录
                Response responseLogin = given().
                        params(interfaceConfig).
                        request().
                        expect().
                        when().
                        post(postType);
                //判断是否成功
                if (responseLogin.statusCode() != 200) {
                    response.sendRedirect("/pages/fail.jspa?pageNotFound=" + responseLogin.statusCode());
                }
                String message = responseLogin.jsonPath().getString("errorMsg");
                if (message.contains("uccess")) {
                    user_token = responseLogin.jsonPath().getString("data.token");
                    userId = responseLogin.jsonPath().getString("data.userid");
                }else{
//                    request.getSession().setAttribute("verification", "expired");
                    request.getSession().removeAttribute("result");
                    request.getSession().setAttribute("sessionId", interfaceId);
                    response.sendRedirect("/pages/interface.jspa?interfaceId="+interfaceId);
                    return;
                }
                //去除sign
                interfaceConfig.remove("code", captch_code);
                interfaceConfig.put("token", user_token);
                interfaceConfig.put("userId", userId);
            }
            //取入参另一部分
            for (Object obj : mapTypes.keySet()){
                if(obj!="") {
                    interfaceConfig.put(obj.toString(), request.getParameter(obj.toString()));
                }
            }

            //对于数组的情况,去掉双引号
//            String interfaceReplace =SignUtil.hashMapToJson(interfaceConfig).replace("\"[","[").replace("]\"","]");
//            sign = SignUtil.sign(interfaceReplace);
//            interfaceConfig.put("sign",sign);
//            String tmp =SignUtil.hashMapToJson(interfaceConfig);
//            tmp = tmp.replace("\"[","[").replace("]\"","]");
            Response responseResult = given().
                    params(interfaceConfig).
                    request().
                    expect().
                    when().
                    post(interfaceName);
            String tmp =SignUtil.hashMapToJson(interfaceConfig);
            System.out.println("tmp="+tmp);
            // 校验请求成功
            if(responseResult.statusCode()!=200){
                response.sendRedirect("/pages/fail.jspa?pageNotFound="+responseResult.statusCode());
            }
            String result = responseResult.jsonPath().get().toString();
            System.out.println("result="+result);
            request.getSession().setAttribute("result", result);
            request.getSession().setAttribute("loginName", loginName);
            request.getSession().setAttribute("loginName_seller",loginName_seller);
            request.getSession().setAttribute("password_seller", password_seller);
            request.getSession().setAttribute("sessionId", interfaceId);
            response.sendRedirect("/pages/interface.jspa?interfaceId="+interfaceId);
        }
}