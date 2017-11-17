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


public class AddUserServlet extends HttpServlet {


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
            String uid = request.getParameter("userid");
            String username = request.getParameter("username");
            String fill_usernick = request.getParameter("fill_usernick");
            String fill_password = request.getParameter("fill_password");
            String email = request.getParameter("emailAddress");
            String discription = request.getParameter("userDiscription");
            String groupname = request.getParameter("groupname");
            String headSculpture = request.getParameter("r1");
            String redirect_url = request.getParameter("redirect_url");
            packageData doPackage = new packageData();

            Object user = request.getSession().getAttribute("uid");
            if(user ==null){
                user =999999;
            }
            if (isdelete == null || isdelete.isEmpty() || isdelete == "") {
                isdelete = "no";
            }

            int login_uid = Integer.parseInt(String.valueOf(user));
            if (login_uid == 999999) {
                response.sendRedirect("/pages/login.jspa");
            } else if (login_uid !=1) {
                response.sendRedirect("/pages/fail.jspa?permission=no");
            } else {

                if (isdelete.equals("yes")) {
                    doPackage.executePackage("delete from user where uid ='" + did+"'" );
                    System.out.println("删除成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/accountList.jspa");
                }else{

                    if (groupname == null || groupname.isEmpty() || groupname == "") {
                        groupname = "tester";
                    }
                    int NOexist = doPackage.selectPackage("select count(*) from user where uid ='" + uid + "'");

                    if (username.length() == 0 || fill_usernick.length() == 0 || fill_password.length() == 0 || email.length() == 0 || discription.length() == 0 || headSculpture.length() == 0) {
                        System.out.println("必填项均不能为空。");
                        response.sendRedirect("/pages/fail.jspa?fillFirst=no");
                    } else if (NOexist >= 1 && uid.length() == 0) {
                        System.out.println("用户已经存在。");
                        response.sendRedirect("/pages/fail.jspa?NOexist=" + NOexist + "");
                    } else if (uid.length() == 0) {
                        //插入数据
                        doPackage.executePackage("insert into user(username,usernick,password,discription,email,groupname,photo) values('" + username + "','" + fill_usernick + "','" + fill_password + "','" + discription + "','" + email + "','" + groupname + "','" + headSculpture + "')");
                        System.out.println("插入成功。");
                        response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url);
                    } else {
                        //更新数据
                        doPackage.executePackage("update user set username ='" + username + "',usernick='" + fill_usernick + "',password='" + fill_password + "',discription='" + discription + "',email='" + email + "',groupname='" + groupname + "',photo='" + headSculpture + "' where uid=" + uid);
                        System.out.println("更新成功。");
                        response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url);
                    }
                }
            }
        }




}
