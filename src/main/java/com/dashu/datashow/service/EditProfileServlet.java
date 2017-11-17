package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.dashu.datashow.controller.packageData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EditProfileServlet extends HttpServlet {


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
        String discription = request.getParameter("discription");
        String email = request.getParameter("email");
        String photo = request.getParameter("r1");
        String uid = request.getParameter("uid");
        String redirect_url = request.getParameter("redirect_url");

        packageData doPackage = new packageData();

            //更新数据
            doPackage.executePackage("update user set discription ='" + discription + "',email='" + email + "',photo='" + photo + "' where uid='" + uid + "'");
            System.out.println("更新个人信息成功。");
            response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
        }




}
