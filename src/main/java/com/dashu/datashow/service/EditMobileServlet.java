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


public class EditMobileServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }


        public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String Date = TimeUtil.getCurrentTime();
        //从页面获取参数
        String id = request.getParameter("id");
        String mobilePlatform = request.getParameter("platform");
        String model = request.getParameter("model");
        String bianhao = request.getParameter("NO").trim();
        String os = request.getParameter("os");
        String user = request.getParameter("user");
        String modify = request.getParameter("modify").replace("T"," ");
        String redirect_url = request.getParameter("redirect_url");

            if(mobilePlatform == null) {
                mobilePlatform = "kong";
            }

        packageData doPackage = new packageData();
        int NOexist = doPackage.selectPackage("select count(*) from machine where nub ='" + bianhao + "'");

        if (mobilePlatform == ""  || model.length() == 0 || user.length() == 0 || os.length() == 0 || bianhao.length() == 0|| modify.length() == 0 ) {
            System.out.println("平台、机型、使用人、编号、手机版本均不能为空。");
            response.sendRedirect("/pages/fail.jspa?mobilePlatform="+mobilePlatform+"&os="+os+"&model="+model+"&user="+user+"&NO="+bianhao+"&modify="+modify+"&NOexist="+NOexist+"");
        } else if (NOexist >= 1&& id.length()== 0&&bianhao.length() != 0 ) {
            System.out.println("编号已经存在。");
            response.sendRedirect("/pages/fail.jspa?NOexist="+NOexist+"");
        } else if(id.length()== 0){
            //插入数据
            doPackage.executePackage("insert into machine(platform,model,createtime,modify,owner,user,nub,os) values('" + mobilePlatform + "','" + model + "','" + Date + "','" + Date + "','" + user + "','" + user + "','" + bianhao + "','" + os + "')");
            System.out.println("插入成功。");
            response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
        }else {
            //更新数据
            doPackage.executePackage("update machine set platform ='" + mobilePlatform + "',model='" + model + "',modify='" + modify + "',user='" + user + "',nub='" + bianhao + "',os='" + os + "' where id=" + id + "");
            doPackage.executePackage("insert into machine_modify_info(mid,modifytime,info) values('" + id + "','" + Date + "','借用人->" + user + ",借用日期->" + modify + "')");
            System.out.println("更新成功。");
            response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
        }
        }




}
