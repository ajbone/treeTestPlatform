package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;


public class AddChannelListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String Date = TimeUtil.getCurrentTime();
        //从页面获取参数
        String id = request.getParameter("id");
        String listName = request.getParameter("listName");
        String[] contentValue = request.getParameterValues("content");
        String appId = request.getParameter("appId");
        Object userNick = request.getSession().getAttribute("userNick");
        String isdelete = request.getParameter("isdelete");
        String did = request.getParameter("did");
        String contentPart ="";
        String[] contentSplit = new String[]{};


        if (isdelete == null || isdelete.isEmpty() || isdelete == "") {
            isdelete = "no";
        }

        packageData doPackage = new packageData();

        Object user = request.getSession().getAttribute("uid");
        if (user == null) {
            response.sendRedirect("/pages/login.jspa?redirect_url=/pages/channelList.jspa");
        } else {

            if (isdelete.equals("yes")) {
                String creater = doPackage.selectStatus("select creater from channelList where id =" + id);
                if (Integer.parseInt(String.valueOf(user)) != 1 && !creater.equals(userNick)) {
                    response.sendRedirect("/pages/fail.jspa?permission=no");
                    return;
                } else {
                    //删除jenkins的job
                    doPackage.executePackage("delete from channelList where id ='" + did + "'");
                    System.out.println("删除成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelList.jspa");
                }
            } else {

                //显示原先值
                if (listName.length() == 0 || contentValue.length == 0) {
                    response.sendRedirect("/pages/fail.jspa?listName=" + listName + "&contentValue=null");
                    return;
                }
                //数组转换成json格式
                for(int i =0;i<contentValue.length;i++) {
                    contentSplit = contentValue[i].split(":");
                    if (i == 0) {
                        contentPart = "\"" + contentSplit[0] + "\":\"" + contentSplit[1] + "\"";
                    } else {
                        contentPart = contentPart + "," +"\"" + contentSplit[0] + "\":\"" + contentSplit[1] + "\"";
                    }
                }
                String contentList ="{"+contentPart+"}";
                if (id.length() == 0) {
                    //插入数据
                    doPackage.executePackage("insert into channelList(createtime,creater,listName,content,appId) values('" + Date + "','" + userNick + "','" + listName + "','" + contentList + "','" + appId + "')");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelList.jspa");
                } else {
                    //更新表
                    doPackage.executePackage("update channelList set createtime ='" + Date + "',creater='" + userNick + "',listName='" + listName + "',content='" + contentList + "',appId='" + appId + "' where id=" + id + "");
                    System.out.println("更新成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelList.jspa");
                }
            }
        }
    }




}
