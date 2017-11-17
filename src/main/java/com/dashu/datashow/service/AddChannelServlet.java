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


public class AddChannelServlet extends HttpServlet {

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
        String channelCode = request.getParameter("channelCode");
        String channel = request.getParameter("channel");
        String appId = request.getParameter("appId");
        Object userNick = request.getSession().getAttribute("userNick");
        String isdelete = request.getParameter("isdelete");
        String did = request.getParameter("did");
        String channelListName = request.getParameter("channelListName");
        String contentPart ="";

        if (isdelete == null || isdelete.isEmpty() || isdelete == "") {
            isdelete = "no";
        }

        packageData doPackage = new packageData();

        Object user = request.getSession().getAttribute("uid");
        if (user == null) {
            response.sendRedirect("/pages/login.jspa?redirect_url=/pages/addChannel.jspa");
        } else {

            if (isdelete.equals("yes")) {
                String creater = doPackage.selectStatus("select creater from channel where id =" + id);
                if (Integer.parseInt(String.valueOf(user)) != 1 && !creater.equals(userNick)) {
                    response.sendRedirect("/pages/fail.jspa?permission=no");
                    return;
                } else {
                    //删除jenkins的job
                    doPackage.executePackage("delete from channel where id ='" + did + "'");
                    System.out.println("删除成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelArrayList.jspa");
                }
            } else {

                //显示原先值
                if (channelCode.length() == 0 || channel.length() == 0) {
                    response.sendRedirect("/pages/fail.jspa?channelCode=" + channelCode + "&channel=" + channel);
                } else if (id.length() == 0) {
                    //批量上传时 根据|切割
                    String[] channelCodeSplit = channelCode.split("\\|");
                    String[] channelSplit = channel.split("\\|");
                    if(channelCodeSplit.length != channelSplit.length){
                        response.sendRedirect("/pages/fail.jspa?style=wrong");
                        return;
                    }
                    //插入数据
                    for(int i =0;i<channelCodeSplit.length;i++) {
                        if(channelCodeSplit[i] ==""||channelSplit[i] ==""){
                            continue;
                        }
                        doPackage.executePackage("insert into channel(createtime,creater,channelCode,channel) values('" + Date + "','" + userNick + "','" + channelCodeSplit[i].trim() + "','" + channelSplit[i].trim() + "')");
                    }
                    //直接产生渠道集合
                    if(channelListName !=null ) {
                        if (channelListName.trim() !="") {
                            for (int i = 0; i < channelCodeSplit.length; i++) {
                                if (i == 0) {
                                    contentPart = "\"" + channelCodeSplit[i].trim() + "\":\"" + channelSplit[i].trim() + "\"";
                                } else {
                                    contentPart = contentPart + "," + "\"" + channelCodeSplit[i].trim() + "\":\"" + channelSplit[i].trim() + "\"";
                                }
                            }
                            String contentList = "{" + contentPart + "}";
                            doPackage.executePackage("insert into channelList(createtime,creater,listName,content,appId) values('" + Date + "','" + userNick + "','" + channelListName + "','" + contentList + "','" + appId + "')");
                        }
                    }

                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelArrayList.jspa");
                } else {
                    //更新表
                    doPackage.executePackage("update channel set createtime ='" + Date + "',creater='" + userNick + "',channelCode='" + channelCode.trim() + "',channel='" + channel.trim() + "' where id=" + id + "");
                    System.out.println("更新成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/channelArrayList.jspa");
                }
            }
        }
    }




}
