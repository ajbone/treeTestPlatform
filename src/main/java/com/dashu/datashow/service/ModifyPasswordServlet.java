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
import java.util.Arrays;


public class ModifyPasswordServlet extends HttpServlet {


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
        char[] oldPassword = request.getParameter("oldPassword").toCharArray();
        String newPassword = request.getParameter("newPassword");
        String newPassword2 = request.getParameter("newPassword2");
        String uid = request.getParameter("uid");
        String redirect_url = request.getParameter("redirect_url");

        packageData doPackage = new packageData();

            //更新数据
            if(!check(uid,oldPassword)) {
                System.out.println("原密码不正确！");
                response.sendRedirect("/pages/fail.jspa?oldPassword=fail");
            }else if ( newPassword == null || newPassword.isEmpty() || newPassword=="" || newPassword2 == null || newPassword2.isEmpty() || newPassword2=="") {
                    System.out.println("新密码不能为空");
                    response.sendRedirect("/pages/fail.jspa?oldPassword=kong");
                } else if (!newPassword.equals(newPassword2)) {
                    System.out.println("新密码两次输入不相同");
                    response.sendRedirect("/pages/fail.jspa?newPassword=notEquals");
                } else {
                    doPackage.executePackage("update user set password ='" + newPassword + "' where uid='" + uid + "'");
                    System.out.println("更新个人信息成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url + "");
                }
            }

    public static boolean check(String uid, char[] password) {
        String result ="";
//        username = StringEscapeUtils.escapeSql(username);// 将用户输入的用户名转义
        packageData doPackage = new packageData();
        String sql = "select password from user where uid = '" + uid + "';";// 定义查询语句
        try {
            result = doPackage.selectStatus(sql);
            char[] queryPassword = result.toCharArray();// 将查询到得密码转换成字符数组
            if (Arrays.equals(password, queryPassword)) {// 如果密码相同则返回true
                Arrays.fill(password, '0');// 清空传入的密码
                Arrays.fill(queryPassword, '0');// 清空查询的密码
                return true;
            } else {// 如果密码不同则返回false
                Arrays.fill(password, '0');// 清空传入的密码
                Arrays.fill(queryPassword, '0');// 清空查询的密码
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;// 如果发生异常返回false
    }




}
