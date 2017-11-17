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


public class AddInterfaceServlet extends HttpServlet {


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
            String isdelete = request.getParameter("isdelete");
            String did = request.getParameter("did");
            String interfaceType = request.getParameter("interfaceType");
            String testType = request.getParameter("testType");

            String interfaceId = request.getParameter("interfaceId");
            String redirect_url = request.getParameter("redirect_url");

            String interfaceName = request.getParameter("interfaceName");
            String interfaceConfig = request.getParameter("interfaceConfig");
            String interfaceDiscription = request.getParameter("interfaceDiscription");
            Object userNick = request.getSession().getAttribute("userNick");
            String Date = TimeUtil.getCurrentTime();
            String interConfig = "";

            packageData doPackage = new packageData();

            if (isdelete == null || isdelete.isEmpty() || isdelete == "") {
                isdelete = "no";
            }

            if (interfaceConfig == null || interfaceConfig.isEmpty() || interfaceConfig == "") {
                interfaceConfig = "";
            }

            Object user = request.getSession().getAttribute("uid");
            if(user ==null){
                response.sendRedirect("/pages/login.jspa?redirect_url=/pages/interfaceList.jspa");
            }  else {

                if (isdelete.equals("yes")) {
                    String creater = doPackage.selectStatus("select creater from interface where id =" + did );
                    if (Integer.parseInt(String.valueOf(user)) !=1 &&!creater.equals(userNick)) {
                        response.sendRedirect("/pages/fail.jspa?permission=no");
                        return;
                    }else {
                        doPackage.executePackage("delete from interface where id ='" + did + "'");
                        System.out.println("删除成功。");
                        response.sendRedirect("/pages/success.jspa?redirect_url=/pages/interfaceList.jspa");
                    }
                }else{

                    if (interfaceName.length() == 0 || interfaceType.length() == 0 ||interfaceDiscription.length() ==0||testType.length()==0) {
//                        System.out.println("必填项均不能为空。");
                        response.sendRedirect("/pages/fail.jspa?fillFirst=no");
                    } else if (interfaceId.length() == 0) {
                        String[] ConfigSplit = interfaceConfig.split("\\|");
                        for(int i =0;i<ConfigSplit.length;i++) {
                            if (i == 0) {
                                interConfig = "\"" + ConfigSplit[i] + "\":\"\"";
                            } else {
                                interConfig = interConfig + "," + "\"" + ConfigSplit[i]  + "\":\"\"";
                            }
                        }
                        interConfig ="{"+interConfig+"}";
                        //插入数据
                        doPackage.executePackage("insert into interface(interface,config,discription,creater,createtime,type,testType) values('" + interfaceName + "','" + interConfig + "','" + interfaceDiscription + "','" + userNick + "','" + Date + "','" + interfaceType + "','" + testType + "')");
//                        System.out.println("插入成功。");
                        response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url);
                    } else {
                        String[] ConfigSplit = interfaceConfig.split("\\|");
                        for(int i =0;i<ConfigSplit.length;i++) {
                            if (i == 0) {
                                interConfig = "\"" + ConfigSplit[i] + "\":\"\"";
                            } else {
                                interConfig = interConfig + "," + "\"" + ConfigSplit[i]  + "\":\"\"";
                            }
                        }
                        interConfig ="{"+interConfig+"}";
                        //更新数据
                        doPackage.executePackage("update interface set interface ='" + interfaceName + "',config='" + interConfig + "',discription='" + interfaceDiscription + "',createtime='" + Date + "',type='"+interfaceType+"',testType='"+testType+"'  where id=" + interfaceId);
//                        System.out.println("更新成功。");
                        response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url);
                    }
                }
            }
        }




}
